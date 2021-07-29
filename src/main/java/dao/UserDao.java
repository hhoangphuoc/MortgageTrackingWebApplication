package dao;

import dao.contracts.IUserDao;
import dao.transaction.Transaction;
import model.Customer;
import model.ServiceProvider;
import model.User;
import model.UserAndToken;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dao.exceptions.DaoDuplicateKeyException;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoNotFoundException;
import dao.exceptions.DaoUnauthorizedException;
import dao.exceptions.DaoAccessException;
import org.apache.commons.lang3.NotImplementedException;


/**
 * The DAO implementation of the User class.
 */
public class UserDao extends BaseDao implements IUserDao {
    public UserDao(Transaction transaction) {
        super(transaction);
    }

    public void modelToStatement
        (PreparedStatement statement, User user)
        throws SQLException
    {
        int i = 1;
        statement.setString(i++, user.getEmail());
        statement.setBytes(i++, user.getPassword_hash());
        statement.setBytes(i++, user.getPassword_salt());
        statement.setString(i++, user.getName());
        statement.setString(i++, user.getSurname());
        statement.setBoolean(i++, user.getIs_staff());
    }
    
    public static User resultToModel
        (ResultSet resultSet)
        throws SQLException
    {
        int i = 1;
        return new User(
            resultSet.getInt(i++), resultSet.getString(i++),
            resultSet.getBytes(i++), resultSet.getBytes(i++),
            resultSet.getString(i++), resultSet.getString(i++),
            resultSet.getBoolean(i++), resultSet.getInt(i++)
        );
    }

    private boolean canAccess(int requested_id, int authenticated_id) throws DaoInternalServerException {
        return true;
        // if (requested_id == authenticated_id){
        //     return true;
        // }
        
        // String query = "SELECT s_id FROM mortgage_request\n"
        //         + "WHERE (s_id = ? OR c_id = ?) AND (s_id = ? OR c_id = ?)";
        // try {
        //     var statement = connection.prepareStatement(query);
        //     int i = 1;
        //     statement.setInt(i++, requested_id);
        //     statement.setInt(i++, requested_id);
        //     statement.setInt(i++, authenticated_id);
        //     statement.setInt(i++, authenticated_id);
        //     return statement.executeQuery().next();
        // } catch (SQLException e){
        //     throw new DaoInternalServerException(e);
        // }
    }

    /**
     * gets a single user from the db.
     * @param id the id of the tuple.
     * @return the User with id or null.
     */
    @Override
    public User get(int id) throws DaoInternalServerException, DaoUnauthorizedException {
        String query = "SELECT u_id, email, password_hash, password_salt, name, surname, is_staff, profile_img " +
                "FROM \"user\" u " +
                "WHERE u.u_id = ? " +
                "LIMIT 1;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                // if (!canAccess(id, transaction.getU_id())){
                //     throw new DaoUnauthorizedException("You are not allowed to access use " + id + " as " + transaction.getU_id());
                // }

                return resultToModel(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }
    public User getByEmail(String email) throws DaoAccessException{
        var user = getByEmailUnchecked(email);
        if (user == null){
            return  null;
        }
        var u_id = user.getId();
        if (!canAccess(u_id, transaction.getU_id())){
            throw new DaoUnauthorizedException("You are not allowed to access use " + u_id + " as " + transaction.getU_id());
        }

        return  user;
    }
    private User getByEmailUnchecked(String email) throws DaoInternalServerException {
        String query = "SELECT u_id, email, password_hash, password_salt, name, surname, is_staff, profile_img " +
                "FROM \"user\" u " +
                "WHERE u.email = ? " +
                "LIMIT 1;";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, email);
            ResultSet resultSet = st.executeQuery();

            if (!resultSet.next()) {
                return  null;
            }
            return resultToModel(resultSet);
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }

    public UserAndToken login(String email, String password)
        throws DaoAccessException
    {
        var authDao = transaction.authenticationDao();
        var user = getByEmailUnchecked(email);
        if (user == null){
            throw new DaoNotFoundException("email not found");
        }
        if (!user.checkPassword(password)){
            throw new DaoUnauthorizedException("password incorrect");
        }
        String token = authDao.login(user.getId());
        transaction.authenticate(token);
        return new UserAndToken(user, token);
    }

    @Override
    public void saveProfilePicture(int userId, int fileId) {
        final String query = "UPDATE \"user\" u SET profile_img = ? WHERE u.u_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, fileId);
            st.setInt(2, userId);
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the list of users in the db.
     * @return list of users in the db.
     */
    @Override
    public List<User> getAll() {
        throw new NotImplementedException("this is a security nightmare");
    }

    /**
     * saves a user to db.
     * @param user to save.
     * @throws DaoAccessException
     */
    @Override
    public void save(User user) throws DaoAccessException {
        final String query = "INSERT INTO \"user\"" +
        "(email, password_hash, password_salt, name,surname, is_staff)" +
        "VALUES (?, ?, ?, ?, ?, ?) RETURNING u_id";

        try {
            PreparedStatement st = connection.prepareStatement(query);
            modelToStatement(st, user);
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                return;
            }
            user.setId(resultSet.getInt(1));
            if (user.getIs_staff()){
                var staff = new ServiceProvider();
                staff.setS_id(user.getId());
                var spDao = transaction.serviceProviderDao();
                spDao.insert(staff);
            } else {
                var customer = new Customer();
                customer.setC_id(user.getId());
                var cDao = transaction.customerDao();
                cDao.insert(customer);
            }
        } catch (SQLException e) {
            throw new DaoDuplicateKeyException(e);
        }
    }


    /**
     * updates a user in the db.
     * @param id the id of the existing object to edit.
     * @param updated the new object to attributes with.
     */
    @Override
    public void update(long id, User updated) {

    }

    /**
     * deletes a user in the db.
     * @param user user to delete.
     */
    @Override
    public void delete(User user) {

    }
}
