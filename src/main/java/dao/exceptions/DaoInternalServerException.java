package dao.exceptions;

public class DaoInternalServerException extends DaoAccessException  {

    public DaoInternalServerException(String msg) {
        super(msg);
    }

    public DaoInternalServerException(Throwable cause) {
        super(cause);
    }
    
}
