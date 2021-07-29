package database;

public class UrlParser{
    int index = 0;
    String url;
    DatabaseInfo parse(String url) throws ParseException, NumberFormatException {
        this.index = 0;
        this.url = url;
        String protocol = this.getBefore("://");
        String username = this.getBefore(":");
        String password = this.getBefore("@");
        String host = this.getBefore(":");
        String portString = this.getBefore("/");
        int port = Integer.parseInt(portString);
        String dbName = this.url.substring(this.index);
        return new DatabaseInfo(
            protocol, host, dbName, port,
            username, password
        );
    }
    String getBefore(String str) throws ParseException {
        int end = this.url.indexOf(str, this.index);
        if (end < 0){
            throw new ParseException();
        }
        String part = this.url.substring(this.index, end);
        this.index = end + str.length();
        return part;
    }
    public static class ParseException extends Exception {

    }
}

