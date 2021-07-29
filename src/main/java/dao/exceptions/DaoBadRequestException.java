package dao.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DaoBadRequestException extends DaoAccessException {

    public DaoBadRequestException(String msg) {
        super(msg);
    }
    public DaoBadRequestException(Throwable cause) {
        super(cause);
    }
    @Override
    protected Status getStatus() {
        return Status.BAD_REQUEST;
    }
}
