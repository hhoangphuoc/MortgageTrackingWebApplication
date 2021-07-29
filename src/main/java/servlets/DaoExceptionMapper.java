package servlets;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import dao.exceptions.DaoAccessException;

@Provider
public class DaoExceptionMapper implements ExceptionMapper<DaoAccessException>
{
    @Override
    public Response toResponse(DaoAccessException exception) {
        System.out.println("EXCEPTION MAPPER RAN!");
        return exception.toResponse();
    }
    
}
