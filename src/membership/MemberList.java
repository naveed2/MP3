package membership;

import communication.Messages.ProcessIdentifier;

import java.util.LinkedList;

public class MemberList {

    private LinkedList<ProcessIdentifier> list;

    void removeProcess(ProcessIdentifier processIdentifier){
        this.list.remove(processIdentifier);
    }

    void addProcess(ProcessIdentifier processIdentifier){
        this.list.add(processIdentifier);
    }

    public MemberList get(){
        return this;
    }

    public MemberList length(){
        return this.length();
    }

}
