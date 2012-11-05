package communication;

/**
 * Created with IntelliJ IDEA.
 * User: naveed
 * Date: 11/4/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */

import membership.MemberList;
import communication.Messages.ProcessIdentifier;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Gossip {

    private Integer numOfTargets;
    private MemberList memberList;
    private AtomicBoolean shouldStop;



    public Gossip(){
        this.shouldStop.set(false);
    }

    public void setNumOfTargets(Integer numOfTargets){
        this.numOfTargets = numOfTargets;
    }

    public void setMemberList(MemberList memberList){
        this.memberList = memberList;
    }

    public MemberList getMemberList(){
        return this.memberList;
    }

    public ProcessIdentifier selectRandomTarget(){
        Random rand = new Random();
        Integer randomTarget = rand.nextInt(this.memberList.length());
        return this.memberList.getMember(randomTarget);
    }

    public void start(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                startInfecting();
            }
        }).start();

    }

    private void startInfecting(){
        while(!shouldStop.get()){
            //TODO: add waiting time
            for(Integer i = 0; i < this.numOfTargets; i++){
                ProcessIdentifier infectedProcess = selectRandomTarget();
                sendMessage(infectedProcess);
            }
        }
    }

    void sendMessage(ProcessIdentifier process){
        new UDPClient().sendMessage(process);
    }



}
