package membership;

import communication.Messages;
import communication.Messages.ProcessIdentifier;
import misc.TimeMachine;

import javax.rmi.CORBA.Tie;
import java.util.Iterator;
import java.util.LinkedList;

public class MemberList implements Iterable<ProcessIdentifier>{

    private LinkedList<ProcessIdentifier> list;
    private LinkedList<ProcState> stateList;
    private LinkedList<Long> timeList;

    public MemberList() {
        list = new LinkedList<ProcessIdentifier>();
        stateList = new LinkedList<ProcState>();
        timeList = new LinkedList<Long>();
    }

    public Integer remove(ProcessIdentifier processIdentifier){
        synchronized (this) {
            int pos = find(processIdentifier);
            if(pos != -1) {
                list.remove(pos);
                stateList.remove(pos);
                return pos;
            } else {
                return -1;
            }
        }
    }

    void add(ProcessIdentifier processIdentifier){
        synchronized (this) {
            list.add(processIdentifier);
            stateList.add(ProcState.available);
            timeList.add(TimeMachine.getTime());
        }
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

    public Long getTime(Integer pos) {
        return timeList.get(pos);
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
        synchronized (this) {
            int pos = 0;
            for(ProcessIdentifier proc : list) {
                if(proc.getId().equals(identifier.getId())) {
                    return pos;
                }
                ++pos;
            }
        }
        return -1;
    }

    public void updateProcessIdentifier(ProcessIdentifier identifier) {
        synchronized(this) {
            Integer pos = find(identifier);
            if(pos == -1) { //add new entry to memberList
                add(identifier);
                return;
            }

            list.set(pos, identifier);
            timeList.set(pos, TimeMachine.getTime());
        }
    }
}
