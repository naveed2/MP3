package membership;


import communication.ProcessIdentifierFactory;
import communication.TCPServer;
import communication.UDPServer;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static communication.Messages.*;

public class Proc {
    private Integer tcpPort;
    private Integer udpPort;
    private TCPServer tcpServer;
    private UDPServer udpServer;
    private Boolean isTCPServerStarted;
    private Boolean isUDPServerStarted;
    private Logger logger = Logger.getLogger(Proc.class);
    private Integer timeStamp;
    private String id;
    private ProcessIdentifier identifier;
    private MemberList memberList;

    public Proc(Integer tcpPort) {
        this.timeStamp = 0;
        this.id = UUID.randomUUID().toString();
        this.tcpPort = tcpPort;
        this.udpPort = tcpPort + 1; // UDPPort is always set to TCPPort+1
        memberList = new MemberList();

        this.isTCPServerStarted = this.isUDPServerStarted = false;
    }

    private void initIdentifier() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String address = addr.getHostAddress();
            identifier = ProcessIdentifierFactory.generateProcessIdentifier(id, address, tcpPort, timeStamp);
            System.out.println(identifier.toString());
        } catch (UnknownHostException e) {
            logger.fatal("Unknown local host", e);
            System.exit(-1);
        }

    }


    public void init() {
        initIdentifier();
        memberList.add(identifier);

        //TODO: before init new serve, old server should be closed. 
        initTCPServer();
        initUDPServer();
    }

    public void initTCPServer() {
        tcpServer = new TCPServer(tcpPort);
        tcpServer.setProc(this);

        if(tcpServer.start()) {
            isTCPServerStarted = true;
            logger.info("TCP Server starts successfully, listening to port " + tcpPort);
        } else {
            System.err.println("TCP Server starts failed, please check configuration");
            logger.fatal("TCP Server starts failed");
        }
    }

    public void initUDPServer() {
        udpServer = new UDPServer(udpPort);
        udpServer.setProc(this);

        if(udpServer.start()) {
            isUDPServerStarted = true;
            logger.info("UDP Server starts successfully, listening to port " + udpPort);
        } else {
            System.err.println("UDP Server starts failed, please check configuration");
            logger.fatal("UDP Server starts failed");
        }
    }

    public ProcessIdentifier getIdentifier() {
        return identifier;
    }

    public String getId() {
        return id;
    }

    public MemberList getMemberList() {
        return memberList;
    }

    public void addProcToMemberList(ProcessIdentifier processIdentifier) {
        memberList.add(processIdentifier);
    }

    public Integer getTimeStamp() {
        synchronized (this) {
            return timeStamp;
        }
    }

    public Integer increaseAndGetTimeStamp() {
        synchronized (this) {
            return ++timeStamp;
        }
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

}
