package unit.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import dao.contracts.IAuthenticationDao;
import dao.contracts.IServiceProviderDao;
import dao.contracts.IUserDao;
import dao.transaction.ITransaction;
import model.UserAndToken;
import org.apache.commons.lang3.NotImplementedException;

import org.junit.jupiter.api.Test;

import dao.exceptions.DaoDuplicateKeyException;
import dao.exceptions.DaoNotFoundException;
import dao.exceptions.DaoUnauthorizedException;
import model.User;

public class TestUserDao extends BaseDaoTest {
    IUserDao dao;
    IServiceProviderDao sdao;
    IAuthenticationDao audao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.userDao();
        sdao = transaction.serviceProviderDao();
        audao = transaction.authenticationDao();
    }

    private User randomUser(){
        var rand = Math.random();
        var user = new User(rand + "@rand.com", "password", ""+rand, "rando", false);
        return user;
    }

    @Test
    public void testSave_DoesNotError() {
        var user = randomUser();
        assertDoesNotThrow(() -> {dao.save(user);});
    }
    private User createSp(){
        var user = randomUser();
        user.setIs_staff(true);
        assertDoesNotThrow(() -> {dao.save(user);});
        return user;
    }
    private User createUser(){
        var user = randomUser();
        assertDoesNotThrow(() -> {dao.save(user);});
        return user;
    }
    private UserAndToken createAndLogin(){
        var user = createUser();
        return assertDoesNotThrow(() -> dao.login(user.getEmail(), "password"));
    }
    private UserAndToken createAndLoginSp(){
        var user = createSp();
        return assertDoesNotThrow(() -> dao.login(user.getEmail(), "password"));
    }

    @Test
    public void testSave_availableToGet(){
        var user = createUser();
        var userAndToken = assertDoesNotThrow(() -> dao.login(user.getEmail(), "password"));
        var token = userAndToken.getToken();

        assertDoesNotThrow(() -> transaction.authenticate(token));

        var queriedUser = assertDoesNotThrow(() -> dao.get(user.getId()));
        assertNotNull(queriedUser);
        user.setId(queriedUser.getId());
        assertEquals(user, queriedUser);
    }

    @Test
    public void testUnauthorized(){
        var user = createUser();
        assertThrows(DaoUnauthorizedException.class ,() -> dao.get(user.getId()));
    }

    @Test
    public void testLogginAsOther(){
        var user = createUser();
        var other = createUser();
        assertDoesNotThrow(() -> dao.login(other.getEmail(), "password"));


        assertThrows(DaoUnauthorizedException.class ,() -> dao.get(user.getId()));
    }
    @Test
    public void testLogginAsSpNotOfMortgage_throws(){
        var user = createUser();
        var sp = createSp();
        assertDoesNotThrow(() -> dao.login(sp.getEmail(), "password"));

        assertThrows(DaoUnauthorizedException.class, () -> dao.get(user.getId()));
    }

    @Test
    public void LoggedInAsSpOfMortgage_get_doesNotThrow(){
        var mortgageStatus = assertDoesNotThrow(() ->
                transaction.mortgageRequestDao().getByMIdWithStatus(mortgage_id)
        );
        var mortgageRequest = mortgageStatus.mortgageRequest;
        transaction.setU_id(mortgageRequest.getS_id());
        assertDoesNotThrow(() -> dao.get(mortgageRequest.getC_id()));
    }

    @Test
    public void testcreateStaffandGet(){
        var user = createAndLoginSp().getUser();

        var queriedUser = assertDoesNotThrow(() -> dao.get(user.getId()));
        assertNotNull(queriedUser);
        user.setId(queriedUser.getId());
        assertEquals(user, queriedUser);
        
        var actual = assertDoesNotThrow(()-> sdao.getBySId(user.getId()));
    	assertNotNull(actual);
    }
    
    @Test
    public void testSave2x_ThrowsDuplicateException(){
        var user = randomUser();
        assertDoesNotThrow(() -> {dao.save(user);});
        var exception = assertThrows(DaoDuplicateKeyException.class, () -> {
            dao.save(user);
        });
        
        String[] keyWords = {"email", "duplicate"};
        String msg = exception.getMessage().toLowerCase();
        for (var keyWord: keyWords){
            assertTrue(msg.contains(keyWord));
        }
    }

    @Test
    public void testGet_ReturnsNull(){
        var user = assertDoesNotThrow(() -> dao.get(-1));
        assertNull(user);
    }
    
    @Test
    public void testisStaffUser(){
        var user = createAndLoginSp().getUser();
        assertEquals(true, user.getIs_staff());
    }
    
    @Test
    public void testgetAll() {
    	assertThrows(NotImplementedException.class, () -> dao.getAll());
    }
    
    @Test
    public void testnotStaffUser(){
        var user = createAndLogin().getUser();
        assertEquals(false, user.getIs_staff());
    }
    
    @Test
    public void testGetByEmail() {
        var expteced = createAndLogin().getUser();
    	var user = assertDoesNotThrow(() -> dao.getByEmail(expteced.getEmail()));
        assertEquals(expteced.getName(), user.getName());
        assertEquals(expteced.getSurname(), user.getSurname());
        assertEquals(expteced.getIs_staff(), user.getIs_staff());
    }
    
    @Test
    public void testLogin() {
    	var exception1 = assertThrows(DaoNotFoundException.class, () -> dao.login("-1", "password"));
    	assertTrue(exception1.getMessage().contains("email not found"));
    	assertEquals(Status.NOT_FOUND, exception1.getStatus());
    	var exception2 = assertThrows(DaoUnauthorizedException.class, () -> dao.login("bram@otte.biz", "1"));
    	assertTrue(exception2.getMessage().contains("password incorrect"));
    	assertEquals(Status.UNAUTHORIZED, exception2.getStatus());
    	var user = assertDoesNotThrow(() -> dao.login("bram@otte.biz", "password"));
    	assertEquals(user.getUser().getEmail(), "bram@otte.biz");
    	Optional<Integer> opuid = assertDoesNotThrow(() -> audao.getU_id(user.getToken()));
    	assertTrue(opuid.toString().contains(""+user.getUser().getId()));
    }
    
    @Test
    public void testRegister() {
    	User newUser = new User();
    	newUser.setEmail("minh@vo.biz");
    	newUser.setId(2420);
    	newUser.setIs_staff(true);
    	newUser.setName("Mn");
    	newUser.setPassword("password");
    	newUser.setSurname("V");
    	assertDoesNotThrow(() ->dao.save(newUser));
    	assertDoesNotThrow(() -> dao.login(newUser.getEmail(), "password"));
    	User actual = assertDoesNotThrow(() ->dao.getByEmail("minh@vo.biz"));
    	assertEquals(newUser.getEmail(), actual.getEmail());
    	assertEquals(newUser.getId(), actual.getId());
    	assertEquals(newUser.getIs_staff(), actual.getIs_staff());
    	assertEquals(newUser.getName(), actual.getName());
    	assertEquals(newUser.getSurname(), actual.getSurname());    	
    }
}
