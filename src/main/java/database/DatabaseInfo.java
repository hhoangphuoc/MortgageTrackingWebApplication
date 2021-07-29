package database;


import database.UrlParser.ParseException;


public class DatabaseInfo{
    String protocol, host, dbName; int port;
    String username, password;
    public String getUrl(){
        return String.format(
            "%s://%s:%d/%s",
            protocol, host, port, dbName
        );
    }
    public String getAuthUrl(){
        return String.format(
            "%s://%s:%s@%s:%d/%s",
            protocol, username, password,
            host, port, dbName
        );
    }
    //#region getters and setters
    public void setProtocol(String protocol){
        this.protocol = protocol;
    }
    public String getProtocol(){
        return this.protocol;
    }
    public String getHost(){
        return this.host;
    }
    public String getDbName(){
        return this.dbName;
    }
    public int getPort(){
        return this.port;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
//#endregion getters and setters
    public DatabaseInfo(
        String protocol,
        String host, String dbName, int port,
        String username, String password
    ){
        this.protocol = protocol;
        this.host = host;
        this.dbName = dbName;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    public static DatabaseInfo fromUrl(String url)
        throws ParseException, NumberFormatException 
    {
        return new UrlParser().parse(url);
    }
}

