package servlets;

import dao.contracts.IChatDao;
import dao.contracts.IStateTimestampDao;
import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoAvailabilityException;
import dao.exceptions.DaoFailedCommitException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "InterestOffer")
@Path("/chat/")
public class ChatResource {
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();

    @GET
    @Path("messages/{userId}")
    public Response getAllMessages(@PathParam("userId") int userId)
            throws DaoAccessException {
        JSONArray arr;

        try (var transaction = transactionFactory.create()) {
            IChatDao chatDao = transaction.chatDao();
            arr = chatDao.get(userId);
        }

        return Response
                .status(Response.Status.OK)
                .entity(arr.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("conversation/{userId}")
    public Response getConversation(@PathParam("userId") int userId) throws DaoAccessException {
        JSONObject obj;

        try (var transaction = transactionFactory.create()) {
            IChatDao chatDao = transaction.chatDao();
            obj = chatDao.getConversation(userId);
        }

        return Response
                .status(Response.Status.OK)
                .entity(obj.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("conversation/{conversationId}/{spId}")
    public Response setConversationAssignee(@PathParam("conversationId") int conversationId, @PathParam("spId") int spId)
            throws DaoAccessException
    {
        try (var transaction = transactionFactory.create()) {
            IChatDao chatDao = transaction.chatDao();
            chatDao.setConversationAssignee(conversationId, spId);
        }

        return Response.accepted().build();
    }

    @GET
    @Path("inbox/{spId}")
    public Response getInbox(@PathParam("spId") int spId) throws DaoAccessException {
        JSONObject obj;

        try (var transaction = transactionFactory.create()) {
            IChatDao chatDao = transaction.chatDao();
            obj = chatDao.getInbox(spId);
        }

        return Response
                .status(Response.Status.OK)
                .entity(obj.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
