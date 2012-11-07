package membership;


import communication.Messages;
import communication.TCPServer;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static communication.Messages.*;

public class Proc {
    private Integer tcpPort;
    private TCPServer tcpServer;
    private Logger logger = Logger.getLogger(Proc.class);
    private Integer timeStamp;
    private String id;
    private ProcessIdentifier identifier;

    public Proc(Integer tcpPort) {
        this.timeStamp = 0;
        this.id = UUID.randomUUID().toString();
        this.tcpPort = tcpPort;
    }

    private void initIdentifier() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String address = addr.getHostAddress();
            identifier = ProcessIdentifier.newBuilder().setId(id).setIP(address).setPort(tcpPort).build();
            System.out.println(identifier.toString());
        } catch (UnknownHostException e) {
            logger.fatal("Unknown local host", e);
            System.exit(-1);
        }

    }


    public void init() {
        initIdentifier();

        tcpServer = new TCPServer(tcpPort);
        if(tcpServer.start()) {
            System.out.println("TCP Server starts successfully");
            logger.info("TCP Server starts successfully");
        } else {
            System.err.println("TCP Server starts failed, please check configuration");
            logger.fatal("TCP Server starts failed");
        }
    }

    public ProcessIdentifier getIdentifier() {
        return identifier;
    }
}
