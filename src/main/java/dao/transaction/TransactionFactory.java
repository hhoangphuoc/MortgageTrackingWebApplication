package dao.transaction;

import dao.exceptions.DaoAvailabilityException;
import database.MyPool;

public class TransactionFactory implements ITransactionFactory {
    MyPool pool;
    public TransactionFactory(MyPool pool){
        this.pool = pool;
    }
    public TransactionFactory(){
        this(MyPool.getInstance());
    }
    private static TransactionFactory instance = new TransactionFactory();
    public static TransactionFactory getInstance(){
        return instance;
    }

    @Override
    public Transaction create() throws DaoAvailabilityException {
        return new Transaction(pool);
    }
}
