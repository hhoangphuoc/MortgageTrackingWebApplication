package dao.exceptions;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;;

public class DaoDuplicateKeyException extends DaoBadRequestException {
//    public DaoDuplicateKeyException(String msg){
//        super(msg);
//    }
    public DaoDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
