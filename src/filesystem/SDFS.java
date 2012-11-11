package filesystem;

import communication.FileIdentifierFactory;
import communication.Messages;
import membership.Proc;
import misc.MiscTool;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.Scanner;

import static communication.Messages.*;

public class SDFS {

    private Proc proc;
    private FileList fileList;
    private static Logger logger = Logger.getLogger(SDFS.class);
    private String rootDirectory;

    public SDFS() {
        fileList = new FileList();
        rootDirectory = "sdfs/";
    }

    public void init() {
        File root = new File(rootDirectory);
        if(!root.exists()) {
            if(!root.mkdir()){
                logger.fatal("Create root directory fails");
                System.exit(-1);
            }
        }
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

    public void addFileLocally(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            return;
        }
        String fileName = file.getName();
        FileIdentifier fileIdentifier = FileIdentifierFactory.generateFileIdentifier(proc.getIdentifier(), fileName);

        copyFile(file, rootDirectory+fileName);

    }

    private void copyFile(File sourceFile, String destination) {
        File destFile = new File(destination);
        FileInputStream fis;

        try {
            fis = new FileInputStream(sourceFile);
        } catch (FileNotFoundException e) {
            logger.error("open source file error", e);
            return;
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(destFile);
        } catch (FileNotFoundException e) {
            logger.error("open dest file error", e);
            return;
        }

        int nextByte;
        try {
            while((nextByte = fis.read()) != -1) {
                fos.write(nextByte);
            }
            fis.close();
            fos.close();
        } catch (IOException e) {
            logger.error("copy error", e);
        }
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        Scanner in = new Scanner(System.in);
//        Proc proc = new Proc(MiscTool.inputPortNumber(in));
        Proc proc = new Proc(20000);
        proc.init();

        SDFS sdfs = new SDFS();
        sdfs.setProc(proc);
        sdfs.init();

        while(true) {
            String filename = MiscTool.inputFileName(in);
            sdfs.addFileLocally(filename);
        }

    }
}
