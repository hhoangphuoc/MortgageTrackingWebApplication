package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import env.DatabaseEnv;

public class MyPool implements AutoCloseable {
    String getSchema(){
        return DatabaseEnv.getInstance().getSchema();
    }

    private ConnectionFailed exception = null;
    public static MyPool getInstance(){
        return TestingPool.getInstance();
    }
    public static void setTesting(){
        TestingPool.setTesting();
    }

    private Lock lock = new ReentrantLock();
    private Condition connAvailable = lock.newCondition();
    private DatabaseInfo database;
    private ClosingStack<Connection> readyConns = new ClosingStack<>(0);
    private int taken = 0;

    private int maxPoolSize = 10;
    public int capacity(){
        return maxPoolSize;
    }
    public int available (){
        return this.capacity() - this.taken();
    }
    public int ready(){
        return this.readyConns.size();
    }
    public int taken(){
        return this.taken;
    }

    
    protected MyPool() {
        try {
            Class.forName("org.postgresql.Driver");
            this.database = queryDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            exception = new ConnectionFailed(e);
        }
        onConstruction();
    }

    protected MyPool(DatabaseInfo databaseInfo) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.database = databaseInfo;
        onConstruction();
    }
    private void onConstruction(){
        System.out.println("schema: " + this.getSchema());
        startAutoCloseTimer();
    }

    private long timeoutNanos = 5_000_000_000L;
    private long maxIdleMillis = 1_000L * 60L * 5L;
    private Timer timer = new Timer();

    private void startAutoCloseTimer(){
        timer.schedule(new TimerTask(){
            public void run() {closeOldConnections();}
        }, maxIdleMillis, maxIdleMillis / 4L);
    }

    private void closeOldConnections(){
        int before = ready();
        lock.lock();
        readyConns.closeBefore(Instant.now().minusMillis(maxIdleMillis));
        lock.unlock();
        int after = ready();
        if (after != before){
            System.out.printf("Closed connection, ready was %d now is %d\n", before, after);
        }
    }

    @Deprecated
    public Connection getConnection() throws ConnectionFailed {
        if (this.exception != null){
            throw this.exception;
        }
        lock.lock();
        try {
            if (this.available() < 1){
                System.out.println("WARNING: pool out of connection");
                this.connAvailable.awaitNanos(timeoutNanos);
                if (this.available() < 1){
                   throw new ConnectionFailed("pool connection timeout");
                }
            }
            Connection conn;
            if (this.ready() > 0){
                conn = readyConns.pop();
            } else {
                conn = this.connect(this.database);
            }
            if (conn == null){
                throw new ConnectionFailed("unable to get connection from pool");
            }
            if (conn.isClosed()){
                conn = this.connect(this.database);
            }
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            this.taken += 1;
            return conn;
        } catch (InterruptedException | SQLException e) {
            throw new ConnectionFailed(e);
        }
        finally {
            lock.unlock();
        }
    }
    @Deprecated
    public void returnConnection(Connection conn) throws SQLException{
        lock.lock();
        try {
            conn.commit();
            this.readyConns.push(conn);
            this.taken -= 1;
            this.connAvailable.signal();
        }
        finally {
            lock.unlock();
        }
        
    }
    public DatabaseInfo getDatabase(){
        return this.database;
    }
    private DatabaseInfo queryDatabase() throws SQLException{
        var databases = DatabaseEnv.getInstance().getDatabases();
        Connection connection = null;
        for (var database: databases){
            connection = connect(database);
            if (connection != null){
                this.readyConns.push(connection);
                return database;
            }
        }
        return null;
    }

    private Connection connect(DatabaseInfo db){
        Connection conn = null;
        if (db == null){
            return null;
        }
        try {
            conn = DriverManager.getConnection(
                "jdbc:" + db.getUrl(),
                db.getUsername(), db.getPassword()
            );
        } catch (SQLTimeoutException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    @Override
    public void close() {
        try {
            lock.lock();
            timer.cancel();
            while (ready() > 0){
                try {
                    readyConns.pop().close();
                } catch (SQLException e){
                    e.printStackTrace();
                } 
            }
        } finally {
            lock.unlock();
        }
    }
}
