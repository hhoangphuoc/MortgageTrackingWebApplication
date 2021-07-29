package dao.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DaoNotFoundException extends DaoAccessException {
    public DaoNotFoundException(String msg){
        super(msg);
    }
//    public DaoNotFoundException(Throwable cause) {
//        super(cause);
//    }
    @Override
    public Status getStatus() {
        return Status.NOT_FOUND;
    }
}
