package servlets;

import java.sql.SQLException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import database.MyPool;
import env.DatabaseEnv;

@Path("database")
public class ServletDataBaseInfo {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getData() throws ClassNotFoundException, SQLException
    {
        var dbs = DatabaseEnv.getInstance().getDatabases();
        String res = "";
        for (var db: dbs){
            res += db.getUrl() + "\n";
        }
        res += "----------------\n";
        res += MyPool.getInstance().getDatabase().getUrl();
        return res;
    }
}
