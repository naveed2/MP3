package communication;

import membership.Proc;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
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
                logger.debug("Received packet: " + Arrays.toString(packet.getData()));
//                Message message = Message.parseFrom(bytes);
//                logger.debug("Received Message: " + message.toString());

            } catch (IOException e) {
                if(e.getMessage().equals("socket close")) {
                    break;
                } else {
                    logger.error("udp server socket exception" + e);
                }
            }

        }
    }


    public void setProc(Proc proc) {
        this.proc = proc;
    }
}
