package unit.dao;

import dao.contracts.IApplicationFormDao;
import dao.contracts.IMortgageCheckDao;
import dao.transaction.ITransaction;
import model.MortgageCheck;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMortgageCheckDao extends BaseDaoTest {
    IMortgageCheckDao dao;
    IApplicationFormDao adao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.mortgageCheckDao();
        adao = transaction.applicationFormDao();
    }

    @Test
    void get_NotNull(){
        var mortgageCheck = assertDoesNotThrow(()->dao.getByMId(this.mortgage_id));
        assertNotNull(mortgageCheck);
    }
    
    @Test
    void testUpdateThenGet_returnsSame() {
        var expected = new MortgageCheck();
        expected.setFailedExplannation("you failed:(");
        expected.setIsCollateralAccepted(true);
        expected.setIsFiniancialObligationsAccepted(true);
        expected.setM_id(this.mortgage_id);
        assertDoesNotThrow(() -> dao.update(expected));

        var actual = assertDoesNotThrow(()->dao.getByMId(expected.getM_id()));
        assertEquals(expected.getM_id(), actual.getM_id());
        assertEquals(expected.getFailedExplannation(), actual.getFailedExplannation());
        assertEquals(expected.getIsCollateralAccepted(), actual.getIsCollateralAccepted());
        assertEquals(expected.getIsFiniancialObligationsAccepted(), actual.getIsFiniancialObligationsAccepted());
        assertEquals(expected.getIsIncomeAccepted(), actual.getIsIncomeAccepted());
    }
}
