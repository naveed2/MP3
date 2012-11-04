package membership;


import main.TCPServer;
import org.apache.log4j.Logger;

public class Proc {
    private Integer port;
    private TCPServer tcpServer;
    private Logger logger = Logger.getLogger(Proc.class);

    public Proc(Integer port) {
        this.port = port;
    }

    public void init() {
        tcpServer = new TCPServer(port);
        if(tcpServer.start()) {
            System.out.println("Server starts successfully");
            logger.info("Server starts successfully");
        } else {
            System.err.println("Server starts failed, please check configuration");
            logger.fatal("Server starts failed");
        }
    }
}
