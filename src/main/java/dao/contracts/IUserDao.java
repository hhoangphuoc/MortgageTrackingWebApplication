package dao.contracts;

import dao.Dao;
import dao.exceptions.DaoAccessException;
import model.User;
import model.UserAndToken;

public interface IUserDao extends Dao<User> {
    User getByEmail(String email)
            throws DaoAccessException;
    UserAndToken login(String email, String password)
            throws DaoAccessException;

    void saveProfilePicture(int userId, int fileId);
}
