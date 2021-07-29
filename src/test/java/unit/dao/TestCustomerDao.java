package unit.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dao.contracts.ICustomerDao;
import dao.transaction.ITransaction;
import model.Customer;

public class TestCustomerDao extends BaseDaoTest {
	ICustomerDao dao;
	
    @Override
    protected void withTransaction(ITransaction transaction) {
    	dao = transaction.customerDao();
    }
    
    @Test
    void insertThenGet_returnsNew() {
    	var expected = new Customer();
    	expected.setC_id(7777);
    	assertDoesNotThrow(()->dao.insert(expected));
    	var actual = assertDoesNotThrow(()-> dao.getByCId(7777));
    	assertEquals(expected.getC_id(), actual.getC_id());
    }
}
