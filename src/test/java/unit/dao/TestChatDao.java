package unit.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dao.contracts.IChatDao;
import dao.transaction.ITransaction;
import model.Message;

import java.util.Map;

public class TestChatDao extends BaseDaoTest{
    IChatDao dao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.chatDao();
    }
    @Test
    protected void testGetNull() {
    	JSONArray object = assertDoesNotThrow(()-> dao.get(-1));
    	assertTrue(object.isEmpty());
    }
    @Test
    protected void testSaveAndGet() {
    	Message msg = new Message();
    	msg.setContent("Hi I'm Minh");
    	msg.setFrom(1);
    	msg.setTo(3);
    	assertDoesNotThrow(()-> dao.save(msg));
		// not sure if this test is complete
    	var msgs = assertDoesNotThrow(()-> dao.get(1));
		var actual = msgs.toList().stream()
			.filter((row) -> {
				assertInstanceOf(Map.class, row);
				var map = (Map<String, Object>)row;
				return (int)map.get("sender_id") == msg.getFrom()
						&& map.get("content").equals(msg.getContent());
			}).findAny();
		assertTrue(actual.isPresent());
    }
}
