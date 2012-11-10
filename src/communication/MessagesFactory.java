package communication;

import membership.MemberList;
import membership.ProcState;
import misc.MiscTool;

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

    public static Message generateSyncProcessMessage(Integer timeStamp, ProcessIdentifier syncMachine, MemberList memberList) {
        SyncProcessesMessage.Builder syncMessageBuilder = SyncProcessesMessage.newBuilder();
        syncMessageBuilder.setSyncingMachine(syncMachine);

        for(int i=0; i<memberList.size(); ++i) {
            if(MiscTool.isTheSameIdentifier(memberList.getProcessIdentifier(i), syncMachine)) {
                syncMessageBuilder.addMembers(ProcessIdentifier.newBuilder()
                        .setId(syncMachine.getId()).setIP(syncMachine.getIP())
                        .setPort(syncMachine.getPort()).setTimestamp(timeStamp));
                continue;
            }
            if(memberList.getState(i) == ProcState.available) {
                syncMessageBuilder.addMembers(memberList.getProcessIdentifier(i));
            }
        }


        SyncProcessesMessage syncMessage = syncMessageBuilder.build();
        return Message.newBuilder()
                .setType(MessageType.SyncProcesses).setSyncProcessesMessage(syncMessage).build();
    }

    public static Message generateHearBeatMessage(Integer timeStamp, ProcessIdentifier fromMachine) {
        HeartBeatMessage.Builder builder = HeartBeatMessage.newBuilder();
        HeartBeatMessage heartBeatMessage = builder.setFromMachine(ProcessIdentifier.newBuilder()
                .setId(fromMachine.getId()).setIP(fromMachine.getIP())
                .setPort(fromMachine.getPort()).setTimestamp(timeStamp).build()).build();

        return Message.newBuilder()
                .setType(MessageType.Heartbeat).setHeartBeatMessage(heartBeatMessage).build();
    }

    public static Message generateSendToMessage(ProcessIdentifier sendToMachine) {
        SendToMessage sendToMessage = SendToMessage.newBuilder()
                .setSendToMachine(sendToMachine).build();
        return Message.newBuilder()
                .setType(MessageType.SendTo).setSendToMessage(sendToMessage).build();
    }


}
