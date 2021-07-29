package database;

public class ConnectionFailed extends Exception {
    public ConnectionFailed(){
        super();
    }
    public ConnectionFailed(String message){
        super(message);
    }
    public ConnectionFailed(Throwable cause){
        super(cause);
    }
}
