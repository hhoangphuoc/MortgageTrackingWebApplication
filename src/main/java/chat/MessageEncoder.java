package chat;

import com.google.gson.Gson;
import model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    private static Gson gson = new Gson();

    @Override
    public String encode(Message o) throws EncodeException {
        return gson.toJson(o);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
