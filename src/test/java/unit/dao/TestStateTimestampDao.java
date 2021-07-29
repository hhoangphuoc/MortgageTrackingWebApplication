package unit.dao;

import dao.contracts.IApplicationFormDao;
import dao.contracts.IStateTimestampDao;
import dao.transaction.ITransaction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class TestStateTimestampDao extends BaseDaoTest {
    IStateTimestampDao dao;
    IApplicationFormDao adao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.stateTimestampDao();
    }

    @Test
    void get_NotNull(){
    	var actual = assertDoesNotThrow(()->dao.getStateTimestamp(this.mortgage_id, "Application"));
        assertNotNull(actual);
    }
    
    @Test
    void get_NullState(){
    	var actual = assertDoesNotThrow(()->dao.getStateTimestamp(this.mortgage_id, null));
        assertNull(actual);
    }
    
    @Test
    void get_InvalidM_id(){
    	var actual = assertDoesNotThrow(()->dao.getStateTimestamp(-1, "Application"));
        assertNull(actual);
    }
    
    @Test
    void updateThenGet_returnsNew(){
    	assertDoesNotThrow(()->dao.updateStateTimestamp("Application", "Documents Check" , this.mortgage_id));
    	var expected = assertDoesNotThrow(()->dao.getStateTimestamp(this.mortgage_id, "Application"));
    	var actual = assertDoesNotThrow(()->dao.getStateTimestamp(this.mortgage_id, "Documents Check"));
    	assertEquals(expected.getM_id(), actual.getM_id());
    	assertEquals("Application", expected.getState());
    	assertEquals("Documents Check", actual.getState());
    	assertEquals(expected.getEnd_time(), actual.getStarted_time());
    }

    @Test
    void testgetPrediction() {
    	var actual = assertDoesNotThrow(()->dao.getPredictions(this.mortgage_id));
        assertNotNull(actual);
    }
}
