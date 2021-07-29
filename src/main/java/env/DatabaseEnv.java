package env;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.annotation.Resource;

import database.DatabaseInfo;
import database.UrlParser.ParseException;

@Resource
public class DatabaseEnv {
    private static DatabaseEnv instance = new DatabaseEnv();
    public static DatabaseEnv getInstance(){
        return instance;
    }
    String schema = "track_and_trace_db";
    public String getSchema(){
        String db_schema = System.getenv("DB_SCHEMA");
        if (db_schema == null){
            db_schema = schema;
        }
        return db_schema;
    }
    private DatabaseInfo getEnvDataBase(){
        final var DATABASE_URL = System.getenv("DATABASE_URL");
        if (DATABASE_URL == null || DATABASE_URL.isEmpty()){
            return null;
        }
        try {
            var db = DatabaseInfo.fromUrl(DATABASE_URL);
            db.setProtocol("postgresql");
            return db;
        } catch (NumberFormatException e){
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private List<DatabaseInfo> databases = new ArrayList<>();
    public DatabaseEnv(){
        var envDatabase = getEnvDataBase();
        if (envDatabase != null){
            databases.add(envDatabase);
        }
        
        databases.add(new DatabaseInfo(
            "postgresql", "bronto.ewi.utwente.nl",
            "dab_di20212b_99", 5432,
            "dab_di20212b_99", "kbDyYBxlj9gVCWBI"
        ));

        databases.add(new DatabaseInfo(
            "postgresql", "localhost",
            "track_and_trace", 5432,
            "mark", "letmein"
        ));
    }
    DatabaseInfo envDatabase = null;
    public List<DatabaseInfo> getDatabases(){
        return this.databases;
    }
}
