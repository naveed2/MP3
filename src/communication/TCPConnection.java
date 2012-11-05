package communication;

import misc.MiscTool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static communication.Messages.JoinMessage;
import static communication.Messages.Message;

public class TCPConnection {
    private Logger logger = Logger.getLogger(TCPConnection.class);
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    public TCPConnection() {

    }

    public TCPConnection setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }

    public TCPConnection updateInputAndOutputStream() throws IOException {
        is = socket.getInputStream();
        os = socket.getOutputStream();
        return this;
    }

    public void startReceiving() {
        while(true) {
            try{
                byte[] bytes = new byte[MiscTool.BUFFER_SIZE];
                int num;
                is = socket.getInputStream();
                num = is.read(bytes);
                Message message = Message.parseFrom(bytes);
                handle(message);
            } catch(IOException e) {
                if(e.getMessage().equals("socket close")) {
                    break;
                } else {
                    logger.error("Receiving message error", e);
                    break;
                }
            }
        }
    }

    public void sendData(byte[] bytes) {
        try {
            os.write(bytes);
        } catch (IOException e) {
            logger.error("Sending TCP packets error" + e);
            e.printStackTrace();
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
