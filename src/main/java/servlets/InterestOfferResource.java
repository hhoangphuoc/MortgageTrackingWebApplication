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

import dao.InterestOfferDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import database.ConnectionFailed;
import io.swagger.annotations.Api;
import model.InterestOffer;

@Api(value = "InterestOffer")
@Path("/interestoffer/{mortgageID}")
public class InterestOfferResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public InterestOffer getInterestOffer(@PathParam("mortgageID") int mortgageID)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create()) {
			return transaction.interestOfferDao().getByMId(mortgageID);
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void updateInterestOffer(InterestOffer interestOffer) throws DaoInternalServerException {
		try (var transaction = transactionFactory.create()) {
			transaction.interestOfferDao().update(interestOffer);
		}
	}
}
