package dao.contracts;

import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;

import java.util.Optional;

public interface IAuthenticationDao {
    Optional<Integer> getU_id(String authToken) throws DaoInternalServerException;
    String login(int u_id) throws DaoInternalServerException;
    void logout(String authToken) throws DaoAccessException;
    void clearExpired() throws DaoAccessException;
}
