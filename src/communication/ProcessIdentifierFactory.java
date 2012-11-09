package communication;

import static communication.Messages.ProcessIdentifier;

public class ProcessIdentifierFactory {
    private ProcessIdentifierFactory() {

    }

    public static ProcessIdentifier generateProcessIdentifier(
            String id, String address, Integer port, Integer timeStamp) {
        ProcessIdentifier identifier;
        identifier = ProcessIdentifier.newBuilder()
                .setId(id).setIP(address).setPort(port).setTimestamp(timeStamp).build();
        return identifier;
    }
}
