package membership;

import communication.Messages;
import communication.Messages.ProcessIdentifier;

import java.util.Iterator;
import java.util.LinkedList;

public class MemberList{

    private LinkedList<ProcessIdentifier> list;

    public MemberList() {
        list = new LinkedList<ProcessIdentifier>();
    }

    void remove(ProcessIdentifier processIdentifier){
        this.list.remove(processIdentifier);
    }

    void add(ProcessIdentifier processIdentifier){
        this.list.add(processIdentifier);
    }

    public LinkedList<ProcessIdentifier> get(){
        return this.list;
    }

    public ProcessIdentifier getProcessIdentifier(Integer i){
        return this.list.get(i);
    }

    public ProcessIdentifier getNextProcessIdentifier(Integer i){
        if(i > (size()-1))
            try {
                throw new Exception("Trying to access out of bound element in list.");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        if(i < (size()-1))
            return this.list.get(i + 1);
        else
            return this.list.getFirst();
    }

    public Integer size(){
        return list.size();
    }

}
