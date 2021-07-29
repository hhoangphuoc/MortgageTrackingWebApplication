package servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import model.ProcessState;
import dao.ProcessStateDao;

@Api(value = "MortgageRequest")
public class MortgageRequestResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;
	int mortgageID;
	
	public MortgageRequestResource(UriInfo uriInfo, HttpServletRequest request, int mortgageID) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.mortgageID = mortgageID;
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void updateDeleteRequest(ProcessState processState)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create()) {
			transaction.processStateDao().updateProcessState(processState);
		}
	}
	
	@DELETE
	public void deleteMortgageRequest() throws DaoInternalServerException {
		System.out.println("Deleting the mortgage...");
		try (var transaction = transactionFactory.create()) {
			transaction.mortgageRequestDao().deleteMortgageRequest(mortgageID);
		}
	}
}
