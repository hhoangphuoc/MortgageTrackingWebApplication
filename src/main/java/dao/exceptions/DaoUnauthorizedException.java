package dao.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DaoUnauthorizedException extends DaoBadRequestException {
    public DaoUnauthorizedException(String msg){
        super(msg);
    }
//    public DaoUnauthorizedException(Throwable cause) {
//        super(cause);
//    }
    @Override
    public Status getStatus() {
        return Status.UNAUTHORIZED;
    }
}
