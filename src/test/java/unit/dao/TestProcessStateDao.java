package unit.dao;

import dao.contracts.IProcessStateDao;
import dao.transaction.ITransaction;
import model.ProcessState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestProcessStateDao extends BaseDaoTest {
    IProcessStateDao dao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.processStateDao();
    }
    @Test
    void get_notNull(){
        var processState = assertDoesNotThrow(()->dao.getProcessState(this.mortgage_id));
        assertNotNull(processState);
    }
    
    @Test
    void get_invalidM_id(){
        var processState = assertDoesNotThrow(()->dao.getProcessState(-1));
        assertNull(processState);
    }
    
    @Test
    void updateThenGet_returnsNew(){
        var expected = new ProcessState();
        expected.setState("Binding Offer");
        expected.setStatus("Processing");
        expected.setMortgageID(this.mortgage_id);
        assertDoesNotThrow(()->dao.updateProcessState(expected));
        var actual = assertDoesNotThrow(()->dao.getProcessState(this.mortgage_id));
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getMortgageID(), actual.getMortgageID());
    }
}
