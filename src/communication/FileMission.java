package communication;

import java.io.File;

import static communication.Messages.*;

public class FileMission {

    public enum MissionType{
        send, get
    }

    private MissionType missionType;
    private String fileName;
    private ProcessIdentifier identifier;
    private File file;

    public FileMission(MissionType missionType) {
        this.missionType = missionType;
    }

    private void init(String fileName, ProcessIdentifier identifier) {
        this.fileName = fileName;
        this.identifier = identifier;
        if(missionType == MissionType.send) {
            file = null;    //TODO: get file
        } else {

        }
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public boolean isSendMission() {
        return missionType == MissionType.send;
    }

    public boolean isGetMission() {
        return missionType == MissionType.get;
    }

    public ProcessIdentifier getIdentifier() {
        return identifier;
    }
}
