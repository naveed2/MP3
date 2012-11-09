package filesystem;

import communication.Messages;

import java.util.LinkedList;
import communication.Messages.FileIdentifier;

/**
 * Created with IntelliJ IDEA.
 * User: naveed
 * Date: 11/7/12
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileList {

    private LinkedList<FileIdentifier> fileList;

    void removeFile(FileIdentifier fileIdentifier){
        this.fileList.remove(fileIdentifier);
    }

    void addFile(FileIdentifier fileIdentifier){
        this.fileList.add(fileIdentifier);
    }

    public FileList get(){
        return this;
    }

    public FileIdentifier getFileEntry(Integer i){
        return this.fileList.get(i);
    }

    public Integer length(){
        return this.length();
    }
}