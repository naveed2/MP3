package communication;

import static communication.message.Messages.Message;

public interface IMessageHandler {
    void handle(Message m);
}
