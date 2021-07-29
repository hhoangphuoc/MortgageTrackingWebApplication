package unit.dao;

import dao.exceptions.DaoAvailabilityException;
import dao.exceptions.DaoFailedCommitException;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransaction;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import database.MyPool;
import model.ApplicationForm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseDaoTest {
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
    ITransaction transaction;
    static int mortgage_id;

    @BeforeAll
    public static void beforeAll() throws DaoInternalServerException {
        MyPool.setTesting();
        
        try (var trans = transactionFactory.create();){
            var ap = new ApplicationForm();
            int defaul = 1200;
            float defaul1 = 1200;
            ap.setC_id(2);
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
            mortgage_id = trans.applicationFormDao().createApplication(ap);
        }

    }

    @BeforeEach
    public void baseBeforeEach() throws DaoAvailabilityException {
        transaction = transactionFactory.create();
        withTransaction(transaction);
    }
    @AfterEach
    public void baseAfterEach() throws DaoFailedCommitException {
        transaction.close();
    }
    protected abstract void withTransaction(ITransaction transaction);
}
