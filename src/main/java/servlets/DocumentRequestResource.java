package servlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoNotFoundException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import model.DocRejection;
import model.DocRequest;
import model.DocRequestCreate;

import java.util.List;

@Api(value = "DocumentRequest")
@Path("document_request")
public class DocumentRequestResource {
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();

    @GET
    @Path("doc_id/{doc_id}")
    public DocRequest get(
        @PathParam("doc_id") int doc_id
    )
        throws DaoInternalServerException, DaoNotFoundException
    {
        try (var transaction = transactionFactory.create()) {
            var docOpt = transaction.documentRequestDao().get(doc_id);
            var doc = docOpt.orElseThrow(()->new DaoNotFoundException(
                "document request with doc_id = " + doc_id + " does not exist"
            ));
            return doc;
        }
    }

    @GET
    @Path("m_id/{m_id}")
    public List<DocRequest> getAllByM_id(
            @PathParam("m_id") int m_id
    )
            throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            return transaction.documentRequestDao().getAllByM_id(m_id);
        }
    }

    @POST
    public int insert(DocRequestCreate newDoc)
        throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            return transaction.documentRequestDao().insert(newDoc);
        }
    }

    @PATCH
    @Path("submit")
    // @Consumes(MediaType.APPLICATION_JSON)
    public void submit(int doc_id)
        throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            transaction.documentRequestDao().submit(doc_id);
        }
    }
    
    @PATCH
    @Path("accept")
    // @Consumes(MediaType.APPLICATION_JSON)
    public void accept(int doc_id)
        throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            transaction.documentRequestDao().accept(doc_id);
        }
    }

    @PATCH
    @Path("reject")
    @Consumes(MediaType.APPLICATION_JSON)
    public void reject(DocRejection docRej)
        throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            transaction.documentRequestDao()
                .reject(docRej.getDoc_id(), docRej.getFailed_explanation());
        }
    }
}
