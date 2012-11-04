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

    MemberList getMemberList(){
        return this.list;
    }
    
}
