package dao.contracts;

import dao.exceptions.DaoAccessException;
import model.Message;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IChatDao {
    void save(Message message) throws DaoAccessException;
    JSONArray get(int userId) throws DaoAccessException;
    JSONObject getInbox(int spId) throws DaoAccessException;

    JSONObject getConversation(int userId);

    void setConversationAssignee(int conversationId, int spId) throws DaoAccessException;
}
