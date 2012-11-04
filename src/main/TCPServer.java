package main;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer {
    private Integer port;
    private ServerSocket serverSocket;
    private Logger logger = Logger.getLogger(TCPServer.class);

    public TCPServer(Integer port) {
        this.port = port;
    }

    public boolean start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
