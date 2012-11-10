package communication;

import java.io.File;

import static communication.Messages.*;

public class FileMission {

    public enum MissionType{
        send, get
    }

    private MissionType missionType;
    private FileIdentifier fileIdentifier;
    private File file;

    public FileMission(MissionType missionType) {
        this.missionType = missionType;
    }

    private void init(FileIdentifier fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
        if(missionType == MissionType.send) {
            file = null;    //TODO: get file
        } else {

        }
    }

    public File getFile() {
        return file;
    }
}
