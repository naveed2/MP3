package communication;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPServer {
    private Integer port;
    private ServerSocket serverSocket;
    private Socket socket;
    private Logger logger = Logger.getLogger(TCPServer.class);
    private AtomicBoolean shouldStop;

    public TCPServer(Integer port) {
        this.port = port;
        this.shouldStop = new AtomicBoolean(false);
    }

    public boolean start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        new Thread(new Runnable() {
            public void run() {
                startListening();
            }
        }).start();

        return true;
    }

    private void startListening() {
        while(!shouldStop.get()) {
            try {
                socket = serverSocket.accept();
                Connection conn = new Connection();
                conn.setSocket(socket);
            } catch (IOException e) {
                logger.error("server socket exception", e);
            }
        }
    }

    private void handleConnection(Connection connection) {
        while(!shouldStop.get()) {
            connection.startReceiving();
        }
    }
}
