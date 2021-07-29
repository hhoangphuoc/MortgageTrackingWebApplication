package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.contracts.ICustomerDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.Customer;
 
public class CustomerDao extends BaseDao implements ICustomerDao {

    public CustomerDao(Transaction transaction) {
        super(transaction);
    }

    public void modelToStatement
        (PreparedStatement statement, Customer customer)
        throws SQLException
    {
        int i = 1;
        statement.setInt(i++, customer.getC_id());
        statement.setDouble(i++, customer.getRequested_amount());
        statement.setDouble(i++, customer.getAprx_gross_income());
        statement.setString(i++, customer.getBank_account());
    }
    protected Customer resultToModel
        (ResultSet resultSet)
        throws SQLException
    {
        var customer = new Customer();
        int i = 1;
        customer.setC_id(resultSet.getInt(i++));
        customer.setRequested_amount(resultSet.getDouble(i++));
        customer.setAprx_gross_income(resultSet.getDouble(i++));
        customer.setBank_account(resultSet.getString(i++));
        
        return customer;
    }


    private final String GET_BY_C_ID_QUERY
            = "SELECT c_id, requested_amount, aprx_gross_income, bank_account\n" +
            "FROM \"customer\"\n" +
            "WHERE c_id = ?\n" +
            "LIMIT 1\n";
    
    public Customer getByCId(int c_id) throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(GET_BY_C_ID_QUERY);
            statement.setInt(1, c_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return this.resultToModel(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }


    private final String INSERT_QUERY
            = "INSERT INTO customer \n" +
            "(c_id, requested_amount, aprx_gross_income, bank_account)\n" +
            "VALUES (?, ?, ?, ?)\n";

    public void insert(Customer customer) throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(INSERT_QUERY);
            this.modelToStatement(statement, customer);
            statement.execute();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }    


    
}
