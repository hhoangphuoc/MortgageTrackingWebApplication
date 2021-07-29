package dao;

import dao.contracts.IChatDao;
import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatDao extends BaseDao implements IChatDao {

    public ChatDao(Transaction transaction) {
        super(transaction);
    }

    public void modelToStatement
            (PreparedStatement statement, Message message)
            throws SQLException
    {
        int i = 1;
        statement.setString(i++, message.getContent());
        statement.setInt(i++, message.getFrom());
    }

    private final String INSERT
            = "INSERT INTO chat_message(body, time, sender, conversation_id) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";

    private final String GET_ALL_BY_CUSTOMER_ID
            = "SELECT c.chat_id, c.body, c.time, c.sender AS sender_id,\n" +
            "o.conversation_id, o.user_id, o.assignee_id, (u.name || ' ' || u.surname) AS sender_name, u.profile_img\n" +
            "FROM chat_message c\n" +
            "INNER JOIN chat_conversation o ON c.conversation_id = o.conversation_id\n" +
            "INNER JOIN \"user\" u ON c.sender = u.u_id\n" +
            "WHERE o.user_id = ? " +
            "ORDER BY c.chat_id";

    final String FETCH_CONVERSATION = "SELECT conversation_id FROM chat_conversation\n" +
            "WHERE user_id = ? OR user_id = ?";

    final String FETCH_CONVERSATION_BY_USER = "SELECT conversation_id, user_id, assignee_id FROM chat_conversation WHERE user_id = ?";

    @Override
    public void save(Message message) throws DaoAccessException {
        final String CREATE_NEW_CONVERSATION = "INSERT INTO chat_conversation(user_id) VALUES (?) RETURNING conversation_id";

        try {
            // check if a conversation exists.
            var statement = connection.prepareStatement(FETCH_CONVERSATION);
            statement.setInt(1, message.getFrom());
            statement.setInt(2, message.getTo());

            ResultSet rs = statement.executeQuery();

            int conversation_id;

            if (rs.next()) {
                conversation_id = rs.getInt(1);
            } else {
                // create new conversation first.
                var createNewConversation = connection.prepareStatement(CREATE_NEW_CONVERSATION);
                createNewConversation.setInt(1, message.getFrom());
                var newConvo = createNewConversation.executeQuery();
                newConvo.next();
                conversation_id = newConvo.getInt(1);
            }
            var stmt = connection.prepareStatement(INSERT);
            this.modelToStatement(stmt, message);
            stmt.setInt(3, conversation_id);
            stmt.execute();

        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    @Override
    public JSONArray get(int userId) throws DaoAccessException {
        JSONArray array = new JSONArray();
        try {
            var statement = connection.prepareStatement(GET_ALL_BY_CUSTOMER_ID);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                JSONObject object = new JSONObject();
                object.accumulate("chat_id", resultSet.getInt(1));
                object.accumulate("content", resultSet.getString(2));
                object.accumulate("time", resultSet.getString(3));
                object.accumulate("sender_id", resultSet.getInt(4));
                object.accumulate("conversation_id", resultSet.getInt(5));
                object.accumulate("user_id", resultSet.getString(6));
                object.accumulate("assignee_id", resultSet.getString(7));
                object.accumulate("sender_name", resultSet.getString(8));
                object.accumulate("profile_img", resultSet.getInt(9));
                array.put(object);
            }
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }

        return array;
    }

    @Override
    public JSONObject getInbox(int spId) throws DaoAccessException {
        JSONObject parent = new JSONObject();
        final String GET_INBOX_BY_ID = "SELECT c.conversation_id, c.chat_hash, c.user_id, c.assignee_id, " +
                "c.last_message, c.seen, c.archived, c.unseen_amount, u.name, u.surname, u.email, u.profile_img " +
                "FROM chat_conversation c INNER JOIN \"user\" u ON c.user_id = u.u_id " +
                "WHERE c.assignee_id IS NULL OR c.assignee_id = ?";
        JSONArray assigned = new JSONArray();
        JSONArray unassigned = new JSONArray();

        try {
            var statement = connection.prepareStatement(GET_INBOX_BY_ID);
            statement.setInt(1, spId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                var assignee_id = resultSet.getInt(4);

                JSONObject object = new JSONObject();
                object.accumulate("conversation_id", resultSet.getInt(1));
                object.accumulate("chat_hash", resultSet.getString(2));
                object.accumulate("user_id", resultSet.getInt(3));
                object.accumulate("assignee_id", assignee_id);
                object.accumulate("last_message", resultSet.getString(5));
                object.accumulate("seen", resultSet.getBoolean(6));
                object.accumulate("archived", resultSet.getBoolean(7));
                object.accumulate("unseen_amount", resultSet.getInt(8));
                object.accumulate("name", resultSet.getString(9));
                object.accumulate("surname", resultSet.getString(10));
                object.accumulate("email", resultSet.getString(11));
                object.accumulate("profile_img", resultSet.getInt(12));

                if (assignee_id > 0) {
                    assigned.put(object);
                } else {
                    unassigned.put(object);
                }
            }
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }

        parent.accumulate("assigned", assigned);
        parent.accumulate("unassigned", unassigned);

        return parent;
    }

    @Override
    public JSONObject getConversation(int userId) {
        JSONObject obj = new JSONObject();

        try {
            var statement = connection.prepareStatement(FETCH_CONVERSATION_BY_USER);
            statement.setInt(1, userId);
            var rs = statement.executeQuery();
            if (rs.next()) {
                obj.accumulate("conversation_id", rs.getInt(1));
                obj.accumulate("user_id", rs.getInt(2));
                obj.accumulate("assignee_id", rs.getInt(3));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return obj;
    }

    @Override
    public void setConversationAssignee(int conversationId, int spId) throws DaoAccessException {
        final String query = "UPDATE chat_conversation SET assignee_id = ? WHERE conversation_id = ?";
        try {
            var stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, spId);
            stmt.setInt(2, conversationId);
            stmt.execute();
        } catch(SQLException e) {
            throw new DaoAccessException(e.getMessage());
        }
    }
}
