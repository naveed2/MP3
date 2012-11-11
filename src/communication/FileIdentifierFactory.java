package communication;

import static communication.Messages.*;

public class FileIdentifierFactory {
    private FileIdentifierFactory() {

    }

    public static FileIdentifier generateFileIdentifier(ProcessIdentifier identifier, String fileName) {
        return FileIdentifier.newBuilder().setFilepath(fileName).setFileStoringProcess(identifier).build();
    }
}
