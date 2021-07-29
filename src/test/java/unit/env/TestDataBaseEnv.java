package unit.env;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import database.DatabaseInfo;
import database.DatabaseInfo.*;
import database.UrlParser.ParseException;
import env.DatabaseEnv;

public class TestDataBaseEnv {
    @Test
    public void testConstructor(){
        DatabaseEnv instance = new DatabaseEnv();
        assertNotNull(instance);
    }
    @Test
    public void testGetInstance(){
        DatabaseEnv instance = DatabaseEnv.getInstance();
        assertNotNull(instance);
    }
    @Test
    public void testGetUrl(){
        String protocol = "jdbc:postgresql";
        String host = "bronto.ewi.utwente.nl";
        String dbName = "db-name";
        int port = 5432;
        String username = "user-name";
        String password = "password";
        var db =new DatabaseInfo(protocol, host, dbName, port, username, password) ;
        assertEquals(protocol, db.getProtocol());
        assertEquals(host, db.getHost());
        assertEquals(dbName, db.getDbName());
        assertEquals(port, db.getPort());
        assertEquals(username, db.getUsername());
        assertEquals(password, db.getPassword());
        
        String expectUrl = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/db-name";
        String url = db.getUrl();
        assertEquals(expectUrl, url);

        String expectAuthUrl = "jdbc:postgresql://user-name:password@bronto.ewi.utwente.nl:5432/db-name";
        String authUrl = db.getAuthUrl();
        assertEquals(expectAuthUrl, authUrl);
    }
    @Test
    public void testFromUrl() throws NumberFormatException, ParseException{
        String protocol = "jdbc:postgresql";
        String host = "bronto.ewi.utwente.nl";
        String dbName = "db-name";
        int port = 5432;
        String username = "user-name";
        String password = "password";
        var expectDb =new DatabaseInfo(protocol, host, dbName, port, username, password);
        var expectAuthURl = expectDb.getAuthUrl();
        var db = DatabaseInfo.fromUrl(expectAuthURl);
        var authUrl = db.getAuthUrl();

        assertEquals(expectAuthURl, authUrl);

    }
}
