package communication;

import membership.Proc;

import java.util.concurrent.atomic.AtomicBoolean;

import static communication.Messages.Message;
import static communication.Messages.ProcessIdentifier;

public class FailureDetector {

    private Proc proc;
    private ProcessIdentifier sendTo, listenFrom;
    private AtomicBoolean shouldStop;
    private Thread listenThread;
    private Integer suspension;

    private static final Integer HEART_BEATING_SEND_DELAY = 200;
    private static final Integer HEART_BEATING_LISTEN_DELAY = 1000;
    private static final Integer MAXIMUM_SUSPENSION = 5;

    public FailureDetector() {
        shouldStop = new AtomicBoolean(false);
        sendTo = listenFrom = null;
        suspension = 0;
    }

    public void setProc(Proc proc) {
        this.proc = proc;
    }

    public void start() {
        startSendHeartBeating();
        startListen();
    }

    public void startSendHeartBeating() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!shouldStop.get()) {
                    try{
                        Thread.sleep(HEART_BEATING_SEND_DELAY);
                    } catch(InterruptedException e) {
                        //do nothing
                    }
                    sendHeartBeatingMessage();
                }
            }
        }).start();
    }

    private void startListen() {
        listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!shouldStop.get()) {
                    try{
                        Thread.sleep(HEART_BEATING_LISTEN_DELAY);
                        if(listenFrom == null) {
                            continue;
                        }
                        ++suspension;
                        if(suspension >= MAXIMUM_SUSPENSION) {
                            proc.getMemberList().setAsToBeDeleted(listenFrom);
                        } else {
                            proc.getMemberList().setAsAvailable(listenFrom);
                        }
                    } catch (InterruptedException e) {
                        suspension = 0;
                    }
                }
            }
        });

        listenThread.start();
    }

    public void onReceivingHeartBeat() {
        listenThread.interrupt();
    }

    public void sendHeartBeatingMessage() {
        if(sendTo == null) {
            return;
        }
        UDPClient udpClient = new UDPClient(sendTo);
        Message heartBeatMessage = MessagesFactory.generateHearBeatMessage(proc.getTimeStamp(), proc.getIdentifier());
        udpClient.sendMessage(heartBeatMessage.toByteArray());
    }

    public void stop() {
        shouldStop.set(true);
    }

    public FailureDetector setSendToMachine(ProcessIdentifier sendToMachine) {
        sendTo = sendToMachine;
        return this;
    }

    public FailureDetector setListenFromMachine(ProcessIdentifier listenFromMachine) {
        listenFrom = listenFromMachine;
        return this;
    }
}
