package unit.dao.transaction;

import org.junit.jupiter.api.Test;

import dao.exceptions.DaoFailedCommitException;
import dao.exceptions.DaoUnauthorizedException;
import dao.transaction.TransactionFactory;
import static org.junit.jupiter.api.Assertions.*;

public class TestTransaction {
    final static TransactionFactory transactionFactory = TransactionFactory.getInstance();
    
    @Test
    void testgetU_id() {
    	var transaction = assertDoesNotThrow(() -> transactionFactory.create());
    	assertThrows(DaoUnauthorizedException.class, () -> transaction.getU_id());
    	transaction.setU_id(1);
    	assertDoesNotThrow(() -> transaction.getU_id());
    	assertDoesNotThrow(() -> transaction.getConnection().close());
    }
    
    @Test
    void testCommitFailed(){
        var transaction = assertDoesNotThrow(() -> transactionFactory.create());
        assertDoesNotThrow(() -> transaction.getConnection().close());
        assertThrows(DaoFailedCommitException.class, () -> transaction.close());
    }
    
    
    @Test
    void testCommitNotFailed(){
        var transaction = assertDoesNotThrow(() -> transactionFactory.create());
        assertDoesNotThrow(() -> transaction.close());
    }
}
