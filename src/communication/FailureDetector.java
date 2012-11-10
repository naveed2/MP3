package communication;

import membership.Proc;

import java.util.concurrent.atomic.AtomicBoolean;

import static communication.Messages.Message;
import static communication.Messages.ProcessIdentifier;

public class FailureDetector {

    private Proc proc;
    private ProcessIdentifier sendTo, listenFrom;
    private AtomicBoolean shouldStop;

    private static final Integer HEART_BEATING_DELAY = 200;

    public FailureDetector() {
        shouldStop = new AtomicBoolean(false);
    }

    public FailureDetector init(Proc proc) {
        this.proc = proc;
        sendTo = listenFrom = null;
        return this;
    }


    public void setProc(Proc proc) {
        this.proc = proc;
    }

    public void start() {
        startSendHeartBeating();
    }

    public void startSendHeartBeating() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!shouldStop.get()) {
                    try{
                        Thread.sleep(HEART_BEATING_DELAY);
                    } catch(InterruptedException e) {
                        //do nothing
                    }
                    sendHeartBeatingMessage();
                }
            }
        }).start();
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
}
