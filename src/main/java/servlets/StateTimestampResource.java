package servlets;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dao.StateTimestampDao;
import dao.contracts.IStateTimestampDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import dao.exceptions.DaoAccessException;
import database.ConnectionFailed;
import io.swagger.annotations.Api;
import model.StateTimestamp;
import org.json.JSONArray;

@Api(value = "StateTimestamp")
@Path("/statetimestamp/{mortgageID}")
public class StateTimestampResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/predictions")
	public List<String> getPredictions(@PathParam("mortgageID") int mortgageID) throws DaoAccessException {
		try (var transaction = transactionFactory.create()) {
			return transaction.stateTimestampDao().getPredictions(mortgageID);
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public StateTimestamp getStateTimestamp(
			@PathParam("mortgageID") int mortgageID, @QueryParam("state") String state
	)
			throws DaoInternalServerException
	{
		System.out.println(state);
		try (var transaction = transactionFactory.create()) {
			return transaction.stateTimestampDao().getStateTimestamp(mortgageID, state);
		}
	}
	
}
