package filesystem;

import communication.FileIdentifierFactory;
import communication.MessagesFactory;
import communication.TCPClient;
import membership.Proc;
import misc.MiscTool;
import misc.TimeMachine;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import static communication.Messages.*;

public class SDFS {

    private Proc proc;
    private FileList fileList;

    private Map<String, Integer> timeStampMap;
    private Map<String, Long> localTimeMap;
    private Map<String, FileState> stateMap;

    private static Logger logger = Logger.getLogger(SDFS.class);
    private String rootDirectory;

    private static final Integer MAX_TIME_DIFFERENCE = 100;
    private static final Integer MIN_TIME_DIFFERENCE = 50;

    public SDFS(String rootDirectory) {
        fileList = new FileList();
        timeStampMap = new HashMap<String, Integer>();
        localTimeMap = new HashMap<String, Long>();
        stateMap = new HashMap<String, FileState>();
        this.rootDirectory = rootDirectory;
    }

    public void init() {
        File root = new File(rootDirectory);
        if(!root.exists()) {
            if(!root.mkdir()){
                logger.fatal("Create root directory fails");
                System.exit(-1);
            }
        } else {
            if(!root.isDirectory()) {
                logger.fatal("sdfs is not directory!!!");
                System.exit(-1);
            }
            loadFilesFromRootDirectory();
        }
    }

    private void loadFilesFromRootDirectory() {
        File root = new File(rootDirectory);
        File[] files = root.listFiles();
        if(files == null) {
            return;
        }
        for(File f : files) {
            addFileLocally(f);
        }
    }

    public File getFile(String fileName) {
        return new File(rootDirectory + fileName);
    }

    public void getRemoteFile(String SDFSFileName, String localFileName){
//        new FileOperations().setProc(proc).get(localFileName, SDFSFileName, fileList);
        FileIdentifier remote, local;
        remote = local = null;

        for(FileIdentifier fileIdentifier : fileList) {
            if(!isAvailable(fileIdentifier)) {
                continue;
            }

            if(fileIdentifier.getFilepath().equals(SDFSFileName)) {
                remote = fileIdentifier;
                if(fileIdentifier.getFileStoringProcess().getId().equals(proc.getId())) {
                    local = fileIdentifier;
                }
            }
        }

        if(local != null) {
            copyFile(getFile(SDFSFileName), localFileName);
            return;
        }

        sendGetMessage(remote);
        startReceivingFile(remote, localFileName);
    }

    private void sendGetMessage(FileIdentifier remote) {
        String address = remote.getFileStoringProcess().getIP() + ":"
                + remote.getFileStoringProcess().getPort();
        TCPClient tcpclient = new TCPClient(address);
        tcpclient.setProc(proc);
        if(tcpclient.connect()) {
            Message m = MessagesFactory.generateGetMessage(remote, proc.getIdentifier());
            tcpclient.sendData(m);
            tcpclient.close();
        }
    }

    private void startReceivingFile(FileIdentifier fileIdentifier, String savedName) {
        try {
            ServerSocket serverSocket = new ServerSocket(proc.getTcpPort()+3);
            Socket socket = serverSocket.accept();
            File file = new File(savedName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            BufferedInputStream bis  = new BufferedInputStream(socket.getInputStream());
//            int nextByte;
//            while((nextByte=bis.read())!=-1) {
//                bos.write(nextByte);
//            }
//            bos.flush();
//            bos.close();
            MiscTool.readFromInputStreamToOutputStream(bis, bos);
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File openFile(String fileName) {
        return new File(rootDirectory + fileName);
    }

    public void setProc(Proc proc) {
        this.proc = proc;
    }

    public FileList getFileList() {
        return fileList;
    }

    public void addFileLocally(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            return;
        }
        String fileName = file.getName();
        FileIdentifier fileIdentifier = FileIdentifierFactory.generateFileIdentifier(
                proc.getIdentifier(), fileName, FileState.available);

        copyFile(file, rootDirectory + fileName);
        addAvailableEntryToFileList(fileIdentifier, proc.getTimeStamp());
    }

    public void addFileLocally(File file) {

        String fileName = file.getName();
        FileIdentifier fileIdentifier = FileIdentifierFactory.generateFileIdentifier(
                proc.getIdentifier(), fileName, FileState.available);

        copyFile(file, rootDirectory + fileName);
        addAvailableEntryToFileList(fileIdentifier, proc.getTimeStamp());
    }

    private void copyFile(File sourceFile, String destination) {
        copyFile(sourceFile, new File(destination));
    }

    private void copyFile(File sourceFile, File destFile) {
        if(sourceFile.getAbsolutePath().equals(destFile.getAbsolutePath())){
            return;
        }

        FileInputStream fis;
        BufferedInputStream bis;
        BufferedOutputStream bos;

        try {
            fis = new FileInputStream(sourceFile);
            bis = new BufferedInputStream(fis);
        } catch (FileNotFoundException e) {
            logger.error("open source file error", e);
            return;
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(fos);
        } catch (FileNotFoundException e) {
            logger.error("open dest file error", e);
            return;
        }

        int nextByte;
        try {
            while((nextByte = bis.read()) != -1) {
                bos.write(nextByte);
            }
            bis.close();
            bos.close();
        } catch (IOException e) {
            logger.error("copy error", e);
        }
    }

    public void addAvailableEntryToFileList(FileIdentifier fileIdentifier, Integer timeStamp) {
        if(fileList.find(fileIdentifier)!=-1){
            String key = generateKey(fileIdentifier);
            if(stateMap.get(key) == FileState.syncing) {
                timeStampMap.put(key, timeStamp);
                localTimeMap.put(key, TimeMachine.getTime());
                stateMap.put(key, FileState.available);
            }
            return;
        }
        addEntryToFileList(fileIdentifier, timeStamp, FileState.available);
    }

    public void addEntryToFileList(FileIdentifier fileIdentifier, Integer timeStamp, FileState state) {
        fileList.addFile(fileIdentifier);
        String key = generateKey(fileIdentifier);
        timeStampMap.put(key, timeStamp);
        localTimeMap.put(key, TimeMachine.getTime());
        stateMap.put(key, state);
    }

    public void addSyncEntryToFileList(FileIdentifier fileIdentifier, Integer timeStamp) {
        if(fileList.find(fileIdentifier)!=-1){
            return;
        }
        addEntryToFileList(fileIdentifier, timeStamp, FileState.syncing);
    }

    private String generateKey(FileIdentifier identifier) {
        return identifier.getFileStoringProcess().getIP()+":"+
                identifier.getFileStoringProcess().getPort()+"/" +
                identifier.getFilepath();
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        Scanner in = new Scanner(System.in);
//        Proc proc = new Proc(MiscTool.inputPortNumber(in));
        Proc proc = new Proc(20000);
        proc.init();

        SDFS sdfs = new SDFS("sdfs/");
        sdfs.setProc(proc);
        sdfs.init();

        while(true) {
            String filename = MiscTool.inputFileName(in);
            sdfs.addFileLocally(filename);
        }
    }

    public Integer getFileTimeStamp(FileIdentifier fileIdentifier) {
        String key = generateKey(fileIdentifier);
        return timeStampMap.get(key);
    }

    public FileState getFileState(FileIdentifier fileIdentifier) {
        String key =generateKey(fileIdentifier);
        return stateMap.get(key);
    }

    public Long getFileLocalTime(FileIdentifier fileIdentifier) {
        String key = generateKey(fileIdentifier);
        return localTimeMap.get(key);
    }

    public boolean isAvailable(FileIdentifier fileIdentifier) {
        String key = generateKey(fileIdentifier);
        return stateMap.containsKey(key) && stateMap.get(key) == FileState.available;
    }

    public boolean isSyncing(FileIdentifier fileIdentifier) {
        String key = generateKey(fileIdentifier);
        return stateMap.containsKey(key);
    }

    public boolean isValid(FileIdentifier fileIdentifier) {
        String key = generateKey(fileIdentifier);
        return stateMap.containsKey(key) &&
                ((stateMap.get(key) == FileState.available) || (stateMap.get(key) == FileState.syncing));
    }

    public void updateFileList() {
        synchronized (this) {
            for(FileIdentifier fileIdentifier : getFileList()) {

                if(fileIdentifier.getFileStoringProcess().getId().equals(proc.getId())
                        && isAvailable(fileIdentifier)) {
                    continue;
                }

                Long diff = TimeMachine.getTime() - localTimeMap.get(generateKey(fileIdentifier));
                if(diff > MAX_TIME_DIFFERENCE) {
                    removeFileIdentifierFromList(fileIdentifier);
                    break;
                } else if(diff > MIN_TIME_DIFFERENCE){
                    setToBeDeleted(fileIdentifier);
                }
            }
        }
    }

    private void setToBeDeleted(FileIdentifier fileIdentifier) {
        synchronized (this){
            String key = generateKey(fileIdentifier);
            stateMap.put(key, FileState.toBeDeleted);
        }
    }


    private void removeFileIdentifierFromList(FileIdentifier fileIdentifier) {
        synchronized (this) {
            fileList.removeFile(fileIdentifier);
            String key = generateKey(fileIdentifier);
            timeStampMap.remove(key);
            localTimeMap.remove(key);
            stateMap.remove(key);
        }
    }


    public void updateFileListEntry(FileIdentifier identifier, Integer timeStamp) {
        synchronized (this) {
            String key = generateKey(identifier);
            timeStampMap.put(key, timeStamp);
            localTimeMap.put(key, TimeMachine.getTime());
            FileState oldState, newState;
            oldState = stateMap.get(key);
            newState = FileState.valueOf(identifier.getFileState());
            if(oldState == FileState.available && newState == FileState.syncing) {
                return;
            }
            stateMap.put(key, FileState.valueOf(identifier.getFileState()));
        }
    }

    public void deleteFile(String fileName) {
        synchronized (this) {
            boolean flag = false;
            LinkedList<ProcessIdentifier> list = new LinkedList<ProcessIdentifier>();
            for(FileIdentifier fileIdentifier : getFileList()) {
                if(!fileIdentifier.getFilepath().equals(fileName)) {
                    continue;
                }

                if(!isValid(fileIdentifier)) {
                    continue;
                }


                ProcessIdentifier processIdentifier = fileIdentifier.getFileStoringProcess();
                if(processIdentifier.getId().equals(proc.getId())) {
                    setToBeDeleted(fileIdentifier);
                    if(isAvailable(fileIdentifier)) {
                        deleteFileLocally(fileName);
                    }
                    flag = true;
                } else {
                    list.add(processIdentifier);
                }
            }

            if(flag) {
                for(ProcessIdentifier processIdentifier : list) {
                    new FileOperations().setProc(proc).sendDeleteMessage(fileName,
                            processIdentifier.getIP(), processIdentifier.getPort());
                }
            }
        }
    }

    private void deleteFileLocally(String fileName) {
        File f = new File(rootDirectory + fileName);
        long startTime = TimeMachine.getTime();
        if(f.delete()) {
            logger.info("successfully delete file: " + f.getName());
        } else {
            logger.error("failed in deleting file: " + f.getName());
        }
        float usingTime = (TimeMachine.getTime() - startTime) /10;
        System.out.println("Del uses time " + usingTime + " seconds");
    }
}
