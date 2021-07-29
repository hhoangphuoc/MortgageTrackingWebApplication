package servlets;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dao.exceptions.DaoAvailabilityException;
import dao.exceptions.DaoFailedCommitException;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import model.MortgageRequest;
import model.MortgageStatus;
import util.ObjectUtil;
import dao.exceptions.DaoAccessException;
import org.json.JSONArray;

@Api(value = "MortgageRequests")
@Path("/mortgagerequests")
public class MortgageListResource {
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;

	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	/**
	 * gets all data used by the service provider.
	 * to visualize all current and closed mortgages.
	 * @return an array with mortgage data.
	 */
	@GET
	@Path("{spId}/sp")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getServiceProviderData(@PathParam("spId") int spId) throws DaoAccessException {
		try (var transaction = transactionFactory.create()) {
			JSONArray arr = transaction.mortgageRequestDao().getMortgageData(spId);
			return Response.status(Response.Status.OK)
					.entity(arr.toString())
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
	}
	
	@GET
	@Path("{customerID}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<MortgageRequest> getAllMortgageRequests(
			@PathParam("customerID") int customerID
	)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create();) {
			return transaction.mortgageRequestDao().getAllMortgageRequestsbyCustomer(customerID);
		}
	}
	
	@GET
	@Path("listcustomermortgages/{staffID}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<MortgageRequest> getAllCustomerMortgageRequests(
			@PathParam("staffID") int staffID
	)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create();) {
			return transaction.mortgageRequestDao().getAllMortgageRequestsbySP(staffID);
		}
	}

	@GET
	@Path("{customerID}/status")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MortgageStatus> getAllMortgageRequestsWithStatus(
			@PathParam("customerID") int customerID
	)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create();) {
			return transaction.mortgageRequestDao().getAllByCustomerWithStatus(customerID);
		}
	}
	
    @Path("{customerID}/{mortgageID}")
    public MortgageRequestResource updateCustomerMortgage(@PathParam("mortgageID") int mid) {
        return new MortgageRequestResource(uriInfo, request, mid);
    }
	
	@GET
	public MortgageStatus getMortgage(@QueryParam("m_id") int m_id) throws DaoAccessException {
		try (var transaction = transactionFactory.create();) {
			var res = transaction.mortgageRequestDao().getByMIdWithStatus(m_id);
			return res;
		}
	}
}
