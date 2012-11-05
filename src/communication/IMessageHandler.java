package communication;

import static communication.Messages.Message;

public interface IMessageHandler {
    void handle(Message m);
}
