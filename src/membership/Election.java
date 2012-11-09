package membership;

import communication.Messages.ProcessIdentifier;
import membership.Proc;
import main.MainEntry;

import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: naveed
 * Date: 11/7/12
 * Time: 5:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class Election {
    private ProcessIdentifier currentProcess;
    private ProcessIdentifier nextProcess;
    private boolean hasForwarded;
    private String leaderID;

    public Election(){
        currentProcess = getCurrentProcess().getIdentifier();
        hasForwarded = false;
    }

    public Proc getCurrentProcess(){
        return new MainEntry().getProc();
    }

    public ProcessIdentifier getNextProcess(){
        MemberList currentMemberList = getMemberList();
        return currentMemberList.getNextProcessIdentifier(currentMemberList.get().indexOf(currentProcess));
    }

    public ProcessIdentifier selectLeader(){
        return new MainEntry().getProc().getMemberList().get().getFirst();
    }


    public void forward(){

    }



    public MemberList getMemberList(){
        //TODO code to get the current memberlist

        return null;
    }





}
