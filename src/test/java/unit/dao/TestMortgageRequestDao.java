package unit.dao;

import dao.contracts.IApplicationFormDao;
import dao.contracts.IMortgageRequestDao;

import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMortgageRequestDao  extends BaseDaoTest {
    IMortgageRequestDao dao;
    IApplicationFormDao adao;
    
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.mortgageRequestDao();
        adao = transaction.applicationFormDao();
    }
    @Test
    public void testGetByMIdWithStatus(){
    	var mortgageStatus = assertDoesNotThrow(() -> dao.getByMIdWithStatus(this.mortgage_id));
        assertNotNull(mortgageStatus);
        assertEquals(this.mortgage_id, mortgageStatus.mortgageRequest.getM_id());
    }
    @Test
    public void testGetAllMortgageRequestsbyCustomer(){
        var mortgageRequests = assertDoesNotThrow(() -> dao.getAllMortgageRequestsbyCustomer(1));
        for (var mortgageRequest : mortgageRequests ) {
        	assertEquals(1, mortgageRequest.getC_id());
        }
    }
    
    @Test
    public void testgetAllByCustomerWithStatus(){
        var mortgageStatuses = assertDoesNotThrow(() -> dao.getAllByCustomerWithStatus(1));
        for (var mortgageStatus : mortgageStatuses ) {
        	assertEquals(1, mortgageStatus.mortgageRequest.getC_id());
        }
    }

    @Test
    public void getAllMortgageRequestsbySP(){
        var requests = assertDoesNotThrow(() -> dao.getAllMortgageRequestsbySP(3));
        for (var mortgageRequest : requests ) {
        	assertEquals(3, mortgageRequest.getS_id());
        }
    }

    @Test
    public void testDeleteMortgage() {
    	int newMortgageID = assertDoesNotThrow(() ->adao.createMortgage(1));    	
    	try {
			dao.deleteMortgageRequest(newMortgageID);
		} catch (DaoInternalServerException e) {
		}
    	var mortgageStatus = assertDoesNotThrow(() -> dao.getByMIdWithStatus(newMortgageID));
    	assertNull(mortgageStatus);
    }
    @Test
    public void testGetMortgageData() {
        var arr = assertDoesNotThrow(() -> dao.getMortgageData(3));
        assertNotNull(arr);
    }
}
