package communication;

import filesystem.FileState;

import static communication.Messages.*;

public class FileIdentifierFactory {
    private FileIdentifierFactory() {

    }

    public static FileIdentifier generateFileIdentifier(ProcessIdentifier identifier, String fileName, FileState fileState) {
        return FileIdentifier.newBuilder().
                setFilepath(fileName).setFileStoringProcess(identifier).setFileState(fileState.toString()).build();
    }
}
