package dao;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import dao.contracts.IAuthenticationDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import util.ObjectUtil;

public class AuthenticationDao extends BaseDao implements IAuthenticationDao {
    private static final Duration duration = Duration.ofDays(1);

    public AuthenticationDao(Transaction transaction) {
        super(transaction);
    }

    public Optional<Integer> getU_id(String authToken) throws DaoInternalServerException {
        try {
            if (authToken == null){
                return Optional.empty();
            }
            var statement = connection.prepareStatement(
                    "SELECT u_id FROM login_tokens WHERE token = ? AND expire > ?"
            );
            var now = Timestamp.from(Instant.now());
            statement.setString(1, authToken);
            statement.setTimestamp(2, now);
            var result = statement.executeQuery();
            if (result.next()){
                return Optional.of(result.getInt(1));
            } else {
                clearExpired();
                return Optional.empty();
            }
        }
        catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    public String login(int u_id) throws DaoInternalServerException {
        String token = randomToken();
        while (hasToken(token)){
            token = randomToken();
        }
        setToken(token, u_id);
        return token;
    }
    private boolean hasToken(String token) throws DaoInternalServerException{
        try {
            var statement = connection.prepareStatement(
                "SELECT token FROM login_tokens WHERE token = ?"
            );
            statement.setString(1, token);
            var result = statement.executeQuery();
            return result.next();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }
    
    private void setToken(String token, int u_id) throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(
                "INSERT INTO login_tokens(token, u_id, expire) VALUES(?,?,?)"
            );
            var expire = Timestamp.from(Instant.now().plus(duration));
            statement.setString(1, token);
            statement.setInt(2, u_id);
            statement.setTimestamp(3, expire);
            statement.execute();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }

    public void logout(String authToken) throws DaoInternalServerException {
        try {
            var statement = connection.prepareStatement(
                    "DELETE from login_tokens WHERE token = ?"
            );
            statement.setString(1, authToken);
            statement.execute();
        }
        catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    public void clearExpired() throws DaoInternalServerException {
        try {
            var statement = connection.prepareStatement(
                    "DELETE FROM login_tokens WHERE expire < ?"
            );
            var now = Timestamp.from(Instant.now());
            statement.setTimestamp(1, now);
            statement.execute();
        }
        catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    int tokenLength = 32;
    private String randomToken(){
        var bytes = new byte[tokenLength];
        var rand = new SecureRandom();
        rand.nextBytes(bytes);
        return ObjectUtil.bytesToHex(bytes);
    }
}
