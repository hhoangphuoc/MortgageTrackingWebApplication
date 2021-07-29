package unit.dao;

import dao.contracts.IDocumentRequestDao;
import dao.transaction.ITransaction;
import model.DocRequest;
import model.DocRequestCreate;
import model.enums.EDocumentStatus;

import model.enums.EDocumentType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDocumentRequestDao extends BaseDaoTest {
    IDocumentRequestDao docDao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        docDao = transaction.documentRequestDao();
    }

    DocRequest create(){
        var expected = new DocRequestCreate();
        expected.setMortgage_id(mortgage_id);
        expected.setDocument_type("Financial obligations");
        var doc_id = assertDoesNotThrow(()->docDao.insert(expected));

        var actualOpt = assertDoesNotThrow(() -> docDao.get(doc_id));
        assertTrue(actualOpt.isPresent());
        var actual = actualOpt.get();
        assertEquals(expected.getMortgage_id(), actual.getMortgage_id());
        assertEquals(expected.getDocument_type(), actual.getDocument_type());

        return actual;
    }

    @Test
    void insertThenGet_returnsSame(){
        create();
    }

    @Test
    void testSubmit(){
        var doc = create();

        assertDoesNotThrow(()->docDao.submit(doc.getDoc_id()));

        var afterOpt = assertDoesNotThrow(() -> docDao.get(doc.getDoc_id()));
        assertTrue(afterOpt.isPresent());
        var after = afterOpt.get();

        assertEquals(EDocumentStatus.SUBMITTED.getValue(), after.getDocument_status());
    }

    @Test
    void testAccept(){
        var doc = create();

        assertDoesNotThrow(()->docDao.accept(doc.getDoc_id()));

        var afterOpt = assertDoesNotThrow(() -> docDao.get(doc.getDoc_id()));
        assertTrue(afterOpt.isPresent());
        var after = afterOpt.get();

        assertEquals(EDocumentStatus.ACCEPTED.getValue(), after.getDocument_status());
    }

    @Test
    void testReject(){
        var doc = create();
        String explanation = "This is fake!";
        assertDoesNotThrow(()->docDao.reject(doc.getDoc_id(), explanation));

        var afterOpt = assertDoesNotThrow(() -> docDao.get(doc.getDoc_id()));
        assertTrue(afterOpt.isPresent());
        var after = afterOpt.get();

        assertEquals(EDocumentStatus.REJECTED.getValue(), after.getDocument_status());
        assertEquals(explanation, after.getFailed_explanation());
    }

    @Test
    void adding1_IncreasesLengthBy1(){
        var before = assertDoesNotThrow(() -> docDao.getAllByM_id(mortgage_id));

        var docCreate = new DocRequestCreate();
        docCreate.setMortgage_id(mortgage_id);
        docCreate.setDocument_type(EDocumentType.COLLATERAL.getValue());

        assertDoesNotThrow(() -> docDao.insert(docCreate));

        var after = assertDoesNotThrow(() -> docDao.getAllByM_id(mortgage_id));

        assertEquals(before.size() + 1, after.size());
    }
}
