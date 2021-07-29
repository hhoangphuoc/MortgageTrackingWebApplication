package database;

import file.Resources;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


//TODO: make previder use other schema
public class TestingPool extends MyPool {
    // for copying schema
    public static void main(String[] args) throws IOException, SQLException, ConnectionFailed {
        var pool = TestingPool.getInstance();
        pool.testingSchema = "track_and_trace_prod";
        pool.setupSchema();
    }

    String testingSchema = "track_and_trace_testing";
    private static TestingPool instance;
    public static TestingPool getInstance(){
        if (instance == null){
            instance = new TestingPool();
        }
        return instance;
    }

    private static boolean testing = false;
    @Override
    public Connection getConnection() throws ConnectionFailed {
        var connection = super.getConnection();
            try {
            if (testing){
                connection.setSchema(testingSchema);
            } else {
                connection.setSchema(this.getSchema());
            }
            return connection;
        } catch (SQLException e) {
            throw new ConnectionFailed(e);
        }
    }
    public static synchronized void setTesting(){
        if (testing){return;}
        testing = true;
        if (testing){
            try {
                TestingPool.getInstance().setupSchema();
            } catch (IOException | SQLException | ConnectionFailed e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final String insertTestingApplicationForm = "";
    
    private String getDropTestSchema(){
        return "DROP SCHEMA IF EXISTS "+ testingSchema + " CASCADE;";
    }
    private void setupSchema() throws IOException, SQLException, ConnectionFailed{
        var resources = Resources.getInstance();
        var copySchema = resources.readText("pgsql/copySchema.pgsql");
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setSchema(getSchema());
            conn.prepareCall(getDropTestSchema() + copySchema).execute();
            var prep = conn.prepareStatement("SELECT clone_schema(?,?)");
            prep.setString(1, getSchema());
            prep.setString(2, testingSchema);
            prep.execute();
            conn.setSchema(testingSchema);
        } finally {
            if (conn != null){
                returnConnection(conn);
            }
        }
    }
}
