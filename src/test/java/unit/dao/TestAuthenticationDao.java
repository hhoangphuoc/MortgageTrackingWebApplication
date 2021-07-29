package unit.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import dao.contracts.IAuthenticationDao;
import dao.transaction.ITransaction;

public class TestAuthenticationDao extends BaseDaoTest {
	IAuthenticationDao dao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.authenticationDao();
    }
    @Test
    void testgetNullUid() {
    	Optional<Integer> u_id = assertDoesNotThrow(() -> dao.getU_id(null));
    	String id = u_id.toString();
    	assertEquals("Optional.empty",id);
    }
    @Test
    void testLoginandGetU_id() {
    	String token = assertDoesNotThrow(() -> dao.login(1));
    	Optional<Integer> u_id = assertDoesNotThrow(() -> dao.getU_id(token));
    	String id = u_id.toString();
    	assertEquals("Optional[1]",id);
    	assertDoesNotThrow(() -> dao.logout(token));
    }
    
    @Test
    void testLogoutandgetUid() {
    	String token = assertDoesNotThrow(() -> dao.login(1));
    	assertDoesNotThrow(() -> dao.logout(token));
    	Optional<Integer> u_id = assertDoesNotThrow(() -> dao.getU_id(token));
    	String id = u_id.toString();
    	assertEquals("Optional.empty",id);
    }
    
    @Test
    void test2userLogin() {
    	String token1 = assertDoesNotThrow(() -> dao.login(1));
    	String token2 = assertDoesNotThrow(() -> dao.login(2));
    	assertNotEquals(token1, token2);
    	assertDoesNotThrow(() -> dao.logout(token1));
    	assertDoesNotThrow(() -> dao.logout(token2));
    }
}
