package communication;

import membership.Proc;
import misc.MiscTool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import static communication.Messages.JoinMessage;
import static communication.Messages.Message;
import static communication.Messages.ProcessIdentifier;

public class TCPConnection {
    private Logger logger = Logger.getLogger(TCPConnection.class);
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private Proc proc;

    public TCPConnection() {

    }

    public TCPConnection setSocket(Socket socket) {
        this.socket = socket;
        tryUpdateInputAndOutputStream();
        return this;
    }

    public TCPConnection setProc(Proc proc) {
        if(proc == null) {
            throw new NullPointerException("null argument");
        }
        this.proc = proc;
        return this;
    }

    public void tryUpdateInputAndOutputStream() {
        try {
            updateInputAndOutputStream();
        } catch(IOException e) {
            //
        }
    }


    public TCPConnection updateInputAndOutputStream() throws IOException {
        is = socket.getInputStream();
        os = socket.getOutputStream();
        return this;
    }

    public void startReceiving() {
        while(true) {
            try{
                byte[] tmpBytes = new byte[MiscTool.BUFFER_SIZE];
                int num;
                is = socket.getInputStream();
                num = is.read(tmpBytes);
                byte[] bytes = new byte[num];
                System.arraycopy(tmpBytes, 0, bytes, 0, num);
                Message message = Message.parseFrom(bytes);
                logger.debug("Received Message: " + message.toString());
                handle(message);
            } catch(IOException e) {
                if(e.getMessage().equals("socket close")) {
                    break;
                } else {
                    logger.error("Receiving message error", e);
                }
            } catch(NegativeArraySizeException e) {
                //ignore it
            }
        }
    }

    public void sendData(byte[] bytes) {
        try {
            os.write(bytes);
        } catch (IOException e) {
            logger.error("Sending TCP packets error" + e);
            e.printStackTrace();
        }
    }

    private void handle(Message m) {
        proc.increaseAndGetTimeStamp();

        switch (m.getType()) {
            case Join:
                JoinMessage joinMessage = m.getJoinMessage();
                ProcessIdentifier joinedMachine = joinMessage.getJoinedMachine();
//                ProcessIdentifier remoteProcessIdentifier = generateRemoteProcessIdentifier(joinedMachine);
//                proc.addProcToMemberList(remoteProcessIdentifier);
                if(!reconstructHeartBeatRing(joinedMachine)) {
                    logger.error("Fail to reconstruct ring, drop the join message");
                    break;
                }

                proc.addProcToMemberList(joinedMachine);
                break;
            
            default:
                break;
        }
    }

    private boolean reconstructHeartBeatRing(ProcessIdentifier joinedMachine) {
        Messages.ProcessIdentifier first = proc.getMemberList().getFirst();
        Messages.ProcessIdentifier last = proc.getMemberList().getLast();
        Messages.Message message;

        //before: last -> first -> second ...
        //after: last -> joinMachine -> first -> second
        TCPClient clientConnectToLast = new TCPClient(last.getIP()+":"+last.getPort());
        clientConnectToLast.setProc(proc);
        if(clientConnectToLast.connect()) {
            message = MessagesFactory.generateSendToMessage(joinedMachine);
            clientConnectToLast.sendData(message.toByteArray());
            clientConnectToLast.close();
        } else {
            return false;
        }

        TCPClient clientConnectToFirst = new TCPClient(joinedMachine.getIP()+":"+joinedMachine.getPort());
        clientConnectToFirst.setProc(proc);
        if(clientConnectToFirst.connect()) {
            message = MessagesFactory.generateSendToMessage(first);
            clientConnectToFirst.sendData(message.toByteArray());
            clientConnectToFirst.close();
        } else {
            return false;
        }

        return true;
    }

    public void close() throws IOException {
        socket.close();
    }

    /**
     * Generate remote process identifier from joinedMachine. We cannot directly use ip address in joinedMachine
     * and port in socket.
     * @param joinedMachine
     * @return
     */
    private ProcessIdentifier generateRemoteProcessIdentifier(ProcessIdentifier joinedMachine) {
        String ip = socket.getInetAddress().getHostAddress();
        return ProcessIdentifierFactory.generateProcessIdentifier(
                joinedMachine.getId(), ip, joinedMachine.getPort(), joinedMachine.getTimestamp());
    }

}
