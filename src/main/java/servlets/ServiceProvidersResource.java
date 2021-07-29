package servlets;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import model.MortgageRequest;
import dao.MortgageRequestDao;

@Api(value = "serviceproviders")
@Path("/serviceproviders")
public class ServiceProvidersResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;
	
	@Path("{serviceproviderID}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<MortgageRequest> getSPMortgageRequest(
			@PathParam("serviceproviderID") String id
	)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create()) {
			return transaction.mortgageRequestDao().getAllMortgageRequestsbySP(Integer.parseInt(id));
		}
	}
}
