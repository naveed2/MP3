package communication;

import main.MainEntry;
import membership.Proc;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static communication.Messages.*;

public class TCPFileServer {

    private Integer fileServerPort;
    private ServerSocket serverSocket;
    private Proc proc;
    private AtomicBoolean shouldStop;
    private Map<UUID, FileMission> missions;

    private static Logger logger = Logger.getLogger(TCPFileServer.class);

    public TCPFileServer (Integer fileServerPort){
        this.fileServerPort = fileServerPort;
        shouldStop = new AtomicBoolean(false);
        missions = new HashMap<UUID, FileMission>();
    }

    public void setProc(Proc proc) {
        this.proc = proc;
    }

    public boolean start(){
        try {
            serverSocket = new ServerSocket(fileServerPort);
        } catch (IOException e) {
            return false;
        } catch (IllegalArgumentException e) {
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
        Socket socket;
        while(!shouldStop.get()) {
            try{
                socket = serverSocket.accept();
                TCPConnection conn = new TCPConnection();
                conn.setSocket(socket).setProc(proc);
            } catch (IOException e) {
                logger.error("tcp server socket exception ", e);
            }
        }
    }

    public void prepareToSend(ProcessIdentifier identifier, FileIdentifier fileIdentifier) {

    }

    public void prepareToGet(ProcessIdentifier identifier, FileIdentifier fileIdentifier) {

    }

    private void handleConnection(TCPConnection conn) {

    }


}
