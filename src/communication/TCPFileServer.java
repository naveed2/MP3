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
    private Map<String, FileMission> missions;

    private static Logger logger = Logger.getLogger(TCPFileServer.class);

    public TCPFileServer (Integer fileServerPort){
        this.fileServerPort = fileServerPort;
        shouldStop = new AtomicBoolean(false);
        missions = new HashMap<String, FileMission>();
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

    public void prepareToSend(ProcessIdentifier identifier, String fileName) {

    }

    public void prepareToGet(ProcessIdentifier identifier, String fileName) {

    }

    private void handleConnection(final TCPConnection conn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String add = conn.getRemoteAddress();
                FileMission mission = missions.get(add);

                if(mission.isGetMission()) {
                    sendFile(mission, conn);
                } else {    //send mission
                    getFile(mission, conn);
                }
            }
        }).start();
    }

    private void sendFile(FileMission mission, TCPConnection conn) {
        conn.readAndWriteToFile(mission.getFileName());
    }

    private void getFile(FileMission mission, TCPConnection conn) {

    }

    public void stop() {
        shouldStop.set(true);
    }


}
