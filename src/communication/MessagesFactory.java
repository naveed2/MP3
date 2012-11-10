package communication;

import membership.MemberList;
import membership.ProcState;

import static communication.Messages.*;

public class MessagesFactory {

    private MessagesFactory() {

    }

    public static Message generateJoinMessage(ProcessIdentifier identifier) {
        JoinMessage joinMessage = JoinMessage.newBuilder().setJoinedMachine(identifier).build();
        return Message.newBuilder().
                setType(Messages.MessageType.Join).setJoinMessage(joinMessage).build();
    }

    public static Message generateJoinMessage(String id, String ip, Integer port, Integer timeStamp) {
        ProcessIdentifier identifier = ProcessIdentifier.newBuilder()
                                        .setId(id).setIP(ip).setPort(port).setTimestamp(timeStamp).build();
        return generateJoinMessage(identifier);
    }

    public static Message generateJoinMessage(String id, String address, Integer timeStamp) {
        String[] str = address.split(":");
        return generateJoinMessage(id, str[0], Integer.parseInt(str[1]), timeStamp);
    }

    public static Message generateSyncProcessMessage(ProcessIdentifier syncMachine, MemberList memberList) {
        SyncProcessesMessage.Builder syncMessageBuilder = SyncProcessesMessage.newBuilder();
        syncMessageBuilder.setSyncingMachine(syncMachine);

        for(int i=0; i<memberList.size(); ++i) {
            if(memberList.getState(i) == ProcState.available) {
                syncMessageBuilder.addMembers(memberList.getProcessIdentifier(i));
            }
        }
        SyncProcessesMessage syncMessage = syncMessageBuilder.build();
        return Message.newBuilder()
                .setType(MessageType.SyncProcesses).setSyncProcessesMessage(syncMessage).build();
    }


}
