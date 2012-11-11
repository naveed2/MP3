package communication;

import filesystem.FileList;
import filesystem.SDFS;
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
            if(MiscTool.isTheSameIdentifier(memberList.get(i), syncMachine)) {
                syncMessageBuilder.addMembers(ProcessIdentifier.newBuilder()
                        .setId(syncMachine.getId()).setIP(syncMachine.getIP())
                        .setPort(syncMachine.getPort()).setTimestamp(timeStamp));
                continue;
            }
            if(memberList.getState(memberList.get(i)) == ProcState.available) {
                syncMessageBuilder.addMembers(memberList.get(i));
            }
//            syncMessageBuilder.addMembers(memberList.get(i));
        }


        SyncProcessesMessage syncMessage = syncMessageBuilder.build();
        return Message.newBuilder()
                .setType(MessageType.SyncProcesses).setSyncProcessesMessage(syncMessage).build();
    }

    public static Message generateSyncFileListMessage(Integer timeStamp, ProcessIdentifier syncMachine,
                                                      SDFS sdfs) {
        SyncFilesListMessage.Builder syncFileListMessageBuilder = SyncFilesListMessage.newBuilder();

        for(FileIdentifier fileIdentifier : sdfs.getFileList()) {
            if(!sdfs.isAvailable(fileIdentifier)) {
                continue;
            }

            if(fileIdentifier.getFileStoringProcess().getId().equals(syncMachine.getId())) {
                Integer t = syncMachine.getTimestamp();
                syncFileListMessageBuilder.addFiles(fileIdentifier).addTimestamp(timeStamp);
            } else {
                syncFileListMessageBuilder.addFiles(fileIdentifier).addTimestamp(sdfs.getFileTimeStamp(fileIdentifier));
            }
        }
        return Message.newBuilder()
                .setType(MessageType.SyncFiles).setSyncFilesMessage(syncFileListMessageBuilder.build()).build();
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

    public static Message generateListenFromMessage(ProcessIdentifier listenFromMachine) {
        ListenFromMessage listenFromMessage = ListenFromMessage.newBuilder()
                .setListenFromMachine(listenFromMachine).build();
        return Message.newBuilder()
                .setType(MessageType.ListenFrom).setListenFrom(listenFromMessage).build();
    }

    public static Message generateGetFileMessage(String SDFSfilepath){
        GetFileMessage getFileMessage = GetFileMessage.newBuilder()
                .setFilepath(SDFSfilepath).build();
        return Message.newBuilder().setType(MessageType.getFile)
                .setGetFileMessage(getFileMessage).build();
    }

    public static Message generatePutFileMessage(String SDFSfilepath, ProcessIdentifier storingProcess){
        PutFileMessage putFileMessage = PutFileMessage.newBuilder()
                .setFilepath(SDFSfilepath)
                .setStoringProcess(storingProcess).build();

        return Message.newBuilder()
                .setType(MessageType.putFile).setPutFileMessage(putFileMessage).build();
    }

    public static Message generateDeletedFileMessage(String SDFSfilepath, ProcessIdentifier deletingProcess){
        DeleteFileMessage deleteFileMessage = DeleteFileMessage.newBuilder()
                .setFilepath(SDFSfilepath)
                .setDeletingProcess(deletingProcess).build();

        return Message.newBuilder()
                .setType(MessageType.deleteFile).setDeleteFileMessage(deleteFileMessage).build();
    }

    public static Message generateReadyToPutFileMessage(String SDFSfilepath, ProcessIdentifier storingProcess){
        ReadyToPutFileMessage readyToPutFileMessage = ReadyToPutFileMessage.newBuilder()
                .setFilepath(SDFSfilepath)
                .setStoringProcess(storingProcess).build();

        return Message.newBuilder()
                .setType(MessageType.readyToPut).setReadyToPutFileMessage(readyToPutFileMessage).build();
    }

    public static Message generateReadyToGetFileMessage(String SDFSfilename, ProcessIdentifier storingProcess){
        ReadyToGetFileMessage readytoGetFileMessage = ReadyToGetFileMessage.newBuilder()
                .setFilepath(SDFSfilename)
                .setStoringProcess(storingProcess).build();

        return Message.newBuilder()
                .setType(MessageType.readyToGet)
                .setReadyToGetFileMessage(readytoGetFileMessage).build();
    }




}
