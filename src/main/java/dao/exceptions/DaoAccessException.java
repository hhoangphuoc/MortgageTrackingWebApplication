package dao.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DaoAccessException extends Exception {
    public DaoAccessException(String msg){
        super(msg);
    }
    public DaoAccessException(Throwable cause){
        super(cause);
    }
    protected Status getStatus(){
        return Status.INTERNAL_SERVER_ERROR;
    }
    @Override
    public String getMessage() {
        return super.getMessage() + "\n(" +
            this.getStatus().getStatusCode() + " " + this.getStatus().name() + ")";
    }
    public Response toResponse(){
        this.printStackTrace();
        return Response.status(getStatus())
            .entity(getMessage())
            .type(MediaType.TEXT_PLAIN)
            .build();
    }
}
