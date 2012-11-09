package membership;

import communication.Messages;
import communication.Messages.ProcessIdentifier;

import java.util.Iterator;
import java.util.LinkedList;

public class MemberList implements Iterable<ProcessIdentifier>{

    private LinkedList<ProcessIdentifier> list;
    private LinkedList<ProcState> stateList;

    public MemberList() {
        list = new LinkedList<ProcessIdentifier>();
    }

    void remove(ProcessIdentifier processIdentifier){
        int pos = find(processIdentifier);
        list.remove(pos);
        stateList.remove(pos);
    }

    void add(ProcessIdentifier processIdentifier){
        list.add(processIdentifier);
        stateList.add(ProcState.available);
    }

    public LinkedList<ProcessIdentifier> getList(){
        return this.list;
    }

    public ProcessIdentifier getProcessIdentifier(Integer i){
        return this.list.get(i);
    }

    public ProcState getState(Integer pos) {
        return stateList.get(pos);
    }

    public ProcessIdentifier getNextProcessIdentifier(Integer i){
        if(i > size() - 1)
            try {
                throw new Exception("Out of bound element access.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        if(i < size() - 1)
            return  this.list.get(i+1);
        else
            return this.list.getFirst();

    }

    public Integer size(){
        return list.size();
    }

    public Iterator<ProcessIdentifier> iterator() {
        return list.iterator();
    }

    public Integer find(ProcessIdentifier identifier) {
        int pos = 0;
        for(ProcessIdentifier proc : list) {
            if(proc.getId().equals(identifier.getId())) {
                return pos;
            }
            ++pos;
        }
        return -1;
    }
}
