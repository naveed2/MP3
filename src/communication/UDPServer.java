package communication;

import membership.Proc;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static communication.Messages.*;

public class UDPServer {
    private Integer udpPort;
    private AtomicBoolean shouldStop;
    private DatagramSocket serverSocket;
    private static Logger logger = Logger.getLogger(UDPServer.class);
    private Proc proc;

    private static final Integer BUFFER_SIZE = 4096;

    public UDPServer(Integer udpPort) {
        this.udpPort = udpPort;
        shouldStop = new AtomicBoolean(false);
    }



    public boolean start() {

        try {
            serverSocket = new DatagramSocket(udpPort);
        } catch (SocketException e) {
            logger.error("udp server binding port error", e);
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                startListening();
            }
        }).start();
        return true;
    }

    private void startListening() {

        while(!shouldStop.get()) {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                serverSocket.receive(packet);
                int len = packet.getLength();
                byte[] bytes = new byte[len];
                System.arraycopy(packet.getData(), 0, bytes, 0, len);
                Message message = Message.parseFrom(bytes);
//                logger.debug("Received Message: " + message.toString());
                handleMessage(message);

            } catch (IOException e) {
                if(e.getMessage().equals("socket close")) {
                    break;
                } else {
                    logger.error("udp server socket exception" + e);
                }
            }

        }
    }

    public void handleMessage(Message m) {
        proc.increaseAndGetTimeStamp();

        switch (m.getType()) {
            case SyncProcesses:
                SyncProcessesMessage syncProcessesMessage = m.getSyncProcessesMessage();
                handleSyncMessage(syncProcessesMessage);
                break;

            case Heartbeat:
                HeartBeatMessage heartBeatMessage = m.getHeartBeatMessage();
                System.out.println("11111");
                break;

            default:
                break;
        }
    }

    public void handleSyncMessage(SyncProcessesMessage spm) {
        List<ProcessIdentifier> list = spm.getMembersList();
        for(ProcessIdentifier identifier : list) {
            proc.getMemberList().updateProcessIdentifier(identifier);
        }
    }


    public void setProc(Proc proc) {
        this.proc = proc;
    }
}
