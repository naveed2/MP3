package membership;


import communication.TCPServer;
import org.apache.log4j.Logger;

public class Proc {
    private Integer tcpPort;
    private TCPServer tcpServer;
    private Logger logger = Logger.getLogger(Proc.class);
    private Integer timeStamp;

    public Proc(Integer port) {
        this.tcpPort = port;
        this.timeStamp = 0;
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
