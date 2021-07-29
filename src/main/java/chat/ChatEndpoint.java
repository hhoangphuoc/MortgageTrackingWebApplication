package chat;

import dao.ChatDao;
import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import model.Message;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import util.ObjectUtil;

@ServerEndpoint(value = "/chat/{id}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class ChatEndpoint {
    private Session session;
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();


    @OnOpen
    public void onOpen(Session session, @PathParam("id") int id) throws IOException {
        // Get session and WebSocket connection
        this.session = session;

        // put the id in the session.
        session.getUserProperties().put("user", id);
    }


    @OnMessage
    public void onMessage(Message message, Session session) throws IOException {
        System.out.println(ObjectUtil.objectToString(message));
        // save the message to the database.
        try (var transaction = transactionFactory.create()) {
            transaction.chatDao().save(message);
        } catch (DaoAccessException  e) {
            e.printStackTrace();
        }
        // if the other user is connected notify them, also send it to ourselves
        Integer userToId = message.getTo();
        Integer userFromId = message.getFrom();
        Predicate<Session> filterCriteria = (sesh) -> userToId.equals(sesh.getUserProperties().get("user")) || userFromId.equals(sesh.getUserProperties().get("user"));
        // filter open sessions based on the destination id.
        session.getOpenSessions()
                .stream()
                .filter(filterCriteria)
                .forEach((sesh) -> {
                    sesh.getAsyncRemote().sendObject(message);
                });
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}