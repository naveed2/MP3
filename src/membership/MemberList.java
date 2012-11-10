package membership;

import communication.Messages;
import communication.Messages.ProcessIdentifier;
import misc.TimeMachine;
import org.apache.log4j.Logger;

import javax.rmi.CORBA.Tie;
import java.util.Iterator;
import java.util.LinkedList;

public class MemberList implements Iterable<ProcessIdentifier>{

    private LinkedList<ProcessIdentifier> list;
    private LinkedList<ProcState> stateList;
    private LinkedList<Long> timeList;

    private static final Integer MAX_TIME_DIFFERENCE = 100;

    private static Logger logger = Logger.getLogger(MemberList.class);

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
                timeList.remove(pos);
                return pos;
            } else {
                return -1;
            }
        }
    }

    public void add(ProcessIdentifier processIdentifier){
        synchronized (this) {
            add(processIdentifier, TimeMachine.getTime());
        }
    }

    public void add(ProcessIdentifier processIdentifier, Long time) {
        synchronized (this) {
            list.add(processIdentifier);
            stateList.add(ProcState.available);
            timeList.add(time);
        }
    }

    public LinkedList<ProcessIdentifier> getList(){
        return this.list;
    }

    public ProcessIdentifier get(Integer pos){
        return this.list.get(pos);
    }

    public void set(Integer pos, ProcessIdentifier identifier) {
        synchronized (this) {
            list.set(pos, identifier);
        }
    }

    public void set(Integer pos, Long time) {
        synchronized (this) {
            timeList.set(pos ,time);
        }
    }

    public void set(Integer pos, ProcState procState) {
        synchronized (this) {
            stateList.set(pos ,procState);
        }
    }

    public ProcessIdentifier getFirst() {
        return list.getFirst();
    }

    public ProcessIdentifier getLast() {
        return list.getLast();
    }

    public ProcState getState(Integer pos) {
        return stateList.get(pos);
    }

    public Long getTime(Integer pos) {
        return timeList.get(pos);
    }

    public Long getTime(ProcessIdentifier identifier) {
        Integer pos = find(identifier);
        if(pos == -1)
            return (long) -1;
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

    public void setAsToBeDeleted(ProcessIdentifier identifier) {
        synchronized (this) {
            Integer pos = find(identifier);
            if(pos == -1){
                logger.error("Wrong identifier to set as TBD: " + identifier.getId());
                return;
            }

            stateList.set(pos, ProcState.toBeDeleted);
        }
    }

    public void setAsAvailable(ProcessIdentifier identifier) {
        synchronized (this) {
            Integer pos = find(identifier);
            if(pos == -1) {
                logger.error("Wrong identifier to set as Available: " + identifier.getId());
                return;
            }

            stateList.set(pos, ProcState.available);
        }
    }

    public void updateMemberList() {
        synchronized (this) {
            for(ProcessIdentifier identifier : list) {
                Integer pos = list.indexOf(identifier);
                if((TimeMachine.getTime() - timeList.get(pos)) > MAX_TIME_DIFFERENCE) {
                    remove(identifier);
               }
            }
        }
    }
}
