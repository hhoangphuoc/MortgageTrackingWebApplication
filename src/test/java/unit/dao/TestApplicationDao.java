package unit.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import dao.contracts.IApplicationFormDao;
import dao.transaction.ITransaction;
import model.ApplicationForm;

public class TestApplicationDao extends BaseDaoTest {
	IApplicationFormDao dao;
	
    @Override
    protected void withTransaction(ITransaction transaction) {
    	dao = transaction.applicationFormDao();
    }
    
    @Test
    void testgetAllApplicationForms() {
    	var apps= assertDoesNotThrow(()-> dao.getAllApplicationForms());
		assertNotNull(apps);
    }
    
    @Test
    void testgetApplicationFormForMortgage() {
    	var app= assertDoesNotThrow(()-> dao.getApplicationFormForMortgage(this.mortgage_id));
		assertEquals(this.mortgage_id, app.getM_id());
    }
    
    @Test
    void testcreateApplicationFormwithNullc_id() {
    	var ap= new ApplicationForm();
        int defaul = 1200;
        float defaul1 = 1200;
        ap.setBankAmount(1200);
        ap.setCollateral1(null);
        ap.setCollateral2(null);
        ap.setCollateral3(null);
        ap.setCreditAmount(1200);
        ap.setCreditInfo("testing");
        ap.setDate(null);
        ap.setDuration(null);
        ap.setGrossIncome(defaul1);
        ap.setHaveCredits(false);
        ap.setHaveOtherDebt(false);
        ap.setHavePartner(false);
        ap.setHaveSpousalMaintenance(false);
        ap.setHaveStudentLoan(false);
        ap.setM_id(0);
        ap.setMonthlyAlimony(defaul1);
        ap.setOtherDebtAmount(defaul1);
        ap.setOtherDebtInfo(null);
        ap.setPartnerGrossIncome(defaul1);
        ap.setPrice1(defaul1);
        ap.setPrice2(defaul1);
        ap.setPrice3(defaul1);
        ap.setReason(null);
        ap.setRequestedMortgageAmount(defaul1);
        ap.setStudentDebt(defaul1);
        ap.setWillPartnerRetire(false);
        ap.setWillRetired(false);
        int m_id = assertDoesNotThrow(()-> dao.createApplication(ap));
        var actualapp= assertDoesNotThrow(()-> dao.getApplicationFormForMortgage(m_id));
        assertEquals(0, actualapp.getC_id());
        
    }
}
