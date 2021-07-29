package unit.database;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import database.ConnectionFailed;
import database.MyPool;

public class TestMyPool {
    @Test
    public void testConnect() throws ClassNotFoundException, SQLException, InterruptedException, ConnectionFailed{
        var pool = MyPool.getInstance();
        var database = pool.getDatabase();
        System.out.println("Database: " + database.getUrl());
        var conn = MyPool.getInstance().getConnection();
        assertNotNull(conn);
        assertFalse(conn.isClosed());
        pool.returnConnection(conn);
    }
}
