package communication;

import static communication.Messages.JoinMessage;
import static communication.Messages.Message;
import static communication.Messages.ProcessIdentifier;

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


}
