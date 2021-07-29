package dao;

import dao.transaction.Transaction;

import java.sql.Connection;

public abstract class BaseDao {
    protected Transaction transaction;
    protected Connection connection;
    public BaseDao(Transaction transaction){
        this.transaction = transaction;
        this.connection = transaction.getConnection();
    }
}
