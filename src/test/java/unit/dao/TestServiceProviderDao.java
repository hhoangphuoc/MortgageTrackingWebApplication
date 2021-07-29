package unit.dao;

import dao.contracts.IServiceProviderDao;
import dao.transaction.ITransaction;
import model.ServiceProvider;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestServiceProviderDao  extends BaseDaoTest {
	IServiceProviderDao dao;
    @Override
    protected void withTransaction(ITransaction transaction) {
    	dao = transaction.serviceProviderDao();
    }
    
    @Test
    void insertThenGet_returnsNew() {
    	var expected = new ServiceProvider();
    	expected.setS_id(8888);
    	assertDoesNotThrow(()->dao.insert(expected));
    	var actual = assertDoesNotThrow(()-> dao.getBySId(8888));
    	assertEquals(expected.getS_id(), actual.getS_id());
    }
    
    @Test
    void Get_invalidS_id() {
    	var actual = assertDoesNotThrow(()-> dao.getBySId(-1));
    	assertNull(actual);
    }
}