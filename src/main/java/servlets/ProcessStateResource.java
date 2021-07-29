package servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import dao.ProcessStateDao;
import dao.StateTimestampDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import model.ProcessState;

@Api(value = "ProcessState")
@Path("/processstate/{mortgageID}")
public class ProcessStateResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;
	@Context
	HttpServletRequest request;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ProcessState getProcessState(@PathParam("mortgageID") int mortgageID)
			throws DaoInternalServerException
	{
		try (var transaction = transactionFactory.create()) {
			return transaction.processStateDao().getProcessState(mortgageID);
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void updateProcessState(ProcessState processState)
			throws DaoInternalServerException
	{
		int mortgageID = processState.getMortgageID();
		try (var transaction = transactionFactory.create()) {
			//check if new state => update StateTimestamp
			String lastState = transaction.processStateDao().getProcessState(mortgageID).getState();
			String currentState = processState.getState();
			if (!(currentState.equals("Application")) && (!(lastState.equals("Done")))) {
				if (!currentState.equals(lastState)) {
					transaction.stateTimestampDao().updateStateTimestamp(lastState, currentState, mortgageID);
				}
			}
			transaction.processStateDao().updateProcessState(processState);
		}
	}
}
