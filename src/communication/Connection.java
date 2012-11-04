package communication;

import misc.MiscTool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static communication.Messages.JoinMessage;
import static communication.Messages.Message;

public class Connection {
    private Logger logger = Logger.getLogger(Connection.class);
    private Socket socket;

    public Connection() {

    }

    public Connection setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }

    public void startReceiving() {
        try{
            byte[] bytes = new byte[MiscTool.BUFFER_SIZE];
            int num;
            InputStream is = socket.getInputStream();
            num = is.read(bytes);
            Message message = Message.parseFrom(bytes);
            handle(message);
        } catch(IOException e) {
            if(e.getMessage().equals("socket close")) {

            } else {
                logger.error("Receiving message error", e);
            }
        }
    }

    private void handle(Message m) {
        switch (m.getType()) {
            case Join:
                JoinMessage joinMessage = m.getJoinMessage();
                //todo: do something
            default:
                break;
        }
    }

}
