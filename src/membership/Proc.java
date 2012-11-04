package membership;


import communication.TCPServer;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Proc {
    private Integer tcpPort;
    private TCPServer tcpServer;
    private Logger logger = Logger.getLogger(Proc.class);
    private Integer timeStamp;
    private String id;

    public Proc(Integer port) {
        this.tcpPort = port;
        this.timeStamp = 0;
        this.id = UUID.randomUUID().toString();
    }

    public void init() {
        tcpServer = new TCPServer(tcpPort);
        if(tcpServer.start()) {
            System.out.println("TCP Server starts successfully");
            logger.info("TCP Server starts successfully");
        } else {
            System.err.println("TCP Server starts failed, please check configuration");
            logger.fatal("TCP Server starts failed");
        }
    }
}
