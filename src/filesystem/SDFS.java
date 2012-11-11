package filesystem;

import membership.Proc;

import java.io.File;

public class SDFS {

    private Proc proc;
    private FileList fileList;

    public SDFS() {

    }

    public void init() {

    }


    public File openFile(String fileName) {
        return new File(fileName);
    }

    public void setProc(Proc proc) {
        this.proc = proc;
    }

    public FileList getFileList() {
        return fileList;
    }
}
