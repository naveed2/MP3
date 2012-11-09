package membership;

import communication.Messages;
import communication.Messages.ProcessIdentifier;

import java.util.Iterator;
import java.util.LinkedList;

public class MemberList implements Iterable<ProcessIdentifier>{

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

    public MemberList get(){
        return this;
    }

    public ProcessIdentifier get(Integer i){
        return this.list.get(i);
    }

    public Integer size(){
        return list.size();
    }

    public Iterator<ProcessIdentifier> iterator() {
        return list.iterator();
    }
}
