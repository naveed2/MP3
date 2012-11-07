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

    public static Message generateJoinMessage(String id, String ip, Integer port) {
        ProcessIdentifier identifier = ProcessIdentifier.newBuilder()
                                        .setId(id).setIP(ip).setPort(port).build();
        return generateJoinMessage(identifier);
    }

    public static Message generateJoinMessage(String id, String address) {
        String[] str = address.split(":");
        return generateJoinMessage(id, str[0], Integer.parseInt(str[1]));
    }


}
