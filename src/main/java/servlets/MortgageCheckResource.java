package servlets;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import dao.MortgageCheckDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import database.ConnectionFailed;
import io.swagger.annotations.Api;
import model.MortgageCheck;

@Api(value = "MortgageCheck")
@Path("/mortgagecheck/{mortgageID}")
public class MortgageCheckResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public MortgageCheck getMortgageCheck(@PathParam("mortgageID") int mortgageID)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create();) {
			return transaction.mortgageCheckDao().getByMId(mortgageID);
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void updateMortgageCheck(MortgageCheck mortgageCheck)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create();) {
			transaction.mortgageCheckDao().update(mortgageCheck);
		}
	}
}
