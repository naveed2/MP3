package communication;

import membership.Proc;
import misc.MiscTool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class TCPClient {

    private String remoteIP;
    private Integer remotePort;
    private TCPConnection tcpConnection;
    private Proc proc;

    private static Logger logger = Logger.getLogger(TCPClient.class);

    public TCPClient(String remoteAddress) {
        if(!MiscTool.isIPAddress(remoteAddress)) {
            throw new IllegalArgumentException("Wrong address format");
        }
        String str[]  = remoteAddress.split(":");
        remoteIP = str[0];
        remotePort = Integer.parseInt(str[1]);
    }

    public TCPClient(String remoteIP, Integer remotePort) {
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
    }


    public boolean connect() {
        try {
            Socket socket = new Socket(remoteIP, remotePort);
            tcpConnection = new TCPConnection();
            tcpConnection.setSocket(socket).setProc(proc);
        } catch (IOException e) {
            if(e.getMessage().equals("Connection refused")) {
                logger.info("connect(): socket connection refused");
            } else {
                logger.error("socket construction error", e);
            }
            return false;
        }
        return true;
    }

    public void close() {
        try {
            tcpConnection.close();
        } catch (IOException e) {
            logger.error("socket close error", e);
        }
    }

    public void sendData(byte[] bytes) {
        tcpConnection.sendData(bytes);
    }

    public void receiveAndSaveData(String localFilepath){
        tcpConnection.receiveAndSaveData(localFilepath);
    }

    public void sendData(String str) {
        sendData(str.getBytes());
    }

    public void sendData(Messages.Message m) {
        sendData(m.toByteArray());
    }

    public void setProc(Proc proc) {
        if(proc == null) {
            throw new NullPointerException("null argument!");
        }
        this.proc = proc;
    }
}
