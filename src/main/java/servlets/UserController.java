package servlets;

import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoUnauthorizedException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import model.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import model.Register;
import model.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.net.URI;

/**
 * CRUD Requests for model class User.
 */
@Api(value = "User")
@Path("/user")
public class UserController {
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
    @Context
    HttpServletRequest request;

    /**
     * gets all information about
     * a single user.
     * @param id the users id, passed as a path parameter.
     * @return A json response with the user.
     * @throws DaoAccessException
     */
    @GET
    @ApiOperation(value = "Returns a single user.")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successfully logged in"),
        @ApiResponse(code = 404, message = "account not found"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response get(
        @PathParam("id") int id,
        @CookieParam(Names.cookieAuthToken) String authToken
    ) 
        throws DaoInternalServerException, DaoUnauthorizedException
    {
        try (var transaction = transactionFactory.create()) {
            // transaction.authenticate(authToken);
            var user = transaction.userDao().get(id);
            if (user == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            return Response.ok(user).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Register body) throws DaoAccessException {
        // create the new user.
        User user = new User();
        user.setName(body.getFirstName());
        user.setSurname(body.getLastName());
        user.setPassword(body.getPassword());
        user.setEmail(body.getEmail());

        // try to save the new user.
        try (var transaction = transactionFactory.create();) {
            transaction.userDao().save(user);
        }
        return Response.created(URI.create(request.getRequestURI())).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "successfully logged in"),
        @ApiResponse(code = 404, message = "account not found"),
        @ApiResponse(code = 401, message = "email or password incorrect")
    })
    public Response login(Login login) throws DaoAccessException {
        try (var transaction = transactionFactory.create();) {
            var userDao = transaction.userDao();
            var userAndToken = userDao.login(login.getEmail(), login.getPassword());
            if (userAndToken == null) {
                return Response
                        .status(Status.NOT_FOUND)
                        .entity("user not found")
                        .build();
            }
            var user = userAndToken.getUser();
            var authToken = userAndToken.getToken();
            return Response
                    .ok(user)
                    .cookie(new NewCookie(Names.cookieAuthToken, authToken))
                    .build();
        }
    }
    @DELETE
    @Path("/logout")
    public void logout(
        @CookieParam(Names.cookieAuthToken) String authToken
    ) 
        throws DaoAccessException
    {
        try (var transaction = transactionFactory.create();) {
            var authDao = transaction.authenticationDao();
            authDao.logout(authToken);
        }
    }
}
