package servlets;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dao.ApplicationFormDao;
import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import model.ApplicationForm;
import model.DocRequestCreate;
import model.enums.EDocumentType;

@Api(value = "Applications")
@Path("/applications")
public class ApplicationFormsResource {
	final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();
	@Context
	UriInfo uriInfo;

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createApplicationForm(ApplicationForm applicationForm) throws DaoInternalServerException {
		try (var transaction = transactionFactory.create()) {
			int m_id = transaction.applicationFormDao().createApplication(applicationForm);
			var docDao = transaction.documentRequestDao();
			docDao.insert(new DocRequestCreate(m_id, EDocumentType.GROSS_INCOME.getValue()));
			docDao.insert(new DocRequestCreate(m_id, EDocumentType.FINANCIAL_OBLIGATIONS.getValue()));
			docDao.insert(new DocRequestCreate(m_id, EDocumentType.COLLATERAL.getValue()));
		}
		return Response.accepted().build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<ApplicationForm> getAllApplications() throws DaoInternalServerException {
		try (var transaction = transactionFactory.create()) {
			return transaction.applicationFormDao().getAllApplicationForms();
		}
	}
	
	@GET
	@Path("{m_id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApplicationForm getApplicationForMortgage(@PathParam("m_id") int mid) throws DaoInternalServerException {
		try (var transaction = transactionFactory.create()) {
			return transaction.applicationFormDao().getApplicationFormForMortgage(mid);
		}
	}
}
