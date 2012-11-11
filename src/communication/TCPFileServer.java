package communication;

import membership.Proc;
import misc.TimeMachine;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static communication.Messages.*;

public class TCPFileServer {

    private Integer fileServerPort;
    private ServerSocket serverSocket;
    private Proc proc;
    private AtomicBoolean shouldStop;
    private Map<String, LinkedList<FileMission> > missions;

    private static Logger logger = Logger.getLogger(TCPFileServer.class);

    public TCPFileServer (Integer fileServerPort){
        this.fileServerPort = fileServerPort;
        shouldStop = new AtomicBoolean(false);
        missions = new HashMap<String, LinkedList<FileMission> >();
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
                handleConnection(conn);
            } catch (IOException e) {
                logger.error("tcp server socket exception ", e);
            }
        }
    }

    public void prepareToSend(ProcessIdentifier identifier, String fileName) {
        FileMission mission = new FileMission(FileMission.MissionType.send);
        mission.init(fileName, identifier);

        addMission(mission, identifier);
    }

    public void prepareToGet(ProcessIdentifier identifier, String fileName) {
        FileMission mission = new FileMission(FileMission.MissionType.get);
        mission.init(fileName, identifier);

        addMission(mission, identifier);
    }

    private void addMission(FileMission mission, ProcessIdentifier identifier) {
        String id = identifier.getId();
        if(missions.containsKey(id)) {
            List<FileMission> list = missions.get(id);
            list.add(mission);
        } else {
            LinkedList<FileMission> list = new LinkedList<FileMission>();
            list.add(mission);
            missions.put(id, list);
        }
    }

    private void handleConnection(final TCPConnection conn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key = conn.readID();
                LinkedList<FileMission> list = missions.get(key);
                FileMission mission;

                try{
                    mission = list.pop();
                } catch(NoSuchElementException e) {
                    logger.error("no element in mission list " + e);
                    return;
                }

                long startTime = TimeMachine.getTime();

                if(mission.isGetMission()) {
                    getFile(mission, conn);
                    float usingTime = TimeMachine.getTime() - startTime;
                    System.out.println("Get an file in server uses " + usingTime + " seconds");
                } else {    //send mission
                    sendFile(mission, conn);
                    float usingTime = TimeMachine.getTime() - startTime;
                    System.out.println("Send an file in server uses " + usingTime + " seconds");
                }
            }
        }).start();
    }

    private void sendFile(FileMission mission, TCPConnection conn) {
        conn.readFileAndSend(mission.getFileName());

        try {
            conn.close();
        } catch (IOException e) {
            logger.error("socket close error ", e);
        }
    }

    //TODO: This is inefficient, improve it later
    private void getFile(FileMission mission, TCPConnection conn) {
        conn.readAndWriteToFile(mission.getFileName());
        proc.getSDFS().addFileLocally(mission.getFileName());

        try {
            conn.close();
        } catch (IOException e) {
            logger.error("socket close error ", e);
        }
    }

    public void stop() {
        shouldStop.set(true);
    }


}
