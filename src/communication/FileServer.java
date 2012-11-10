package communication;

import main.MainEntry;
import membership.Proc;

/**
 * Created with IntelliJ IDEA.
 * User: naveed
 * Date: 11/10/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileServer {

    private Integer fileServerPort;
    private TCPServer tcpServer;
    private Proc proc;

    public void FileServer(){
        fileServerPort = (new MainEntry().getProc().getTcpPort()) + 2;
        tcpServer = new TCPServer(fileServerPort);
    }

    private void startProc(){
        proc = new Proc(fileServerPort);
        proc.init();
    }

    public void start(){
        startProc();
        tcpServer.setProc(proc);
        tcpServer.start();
    }
}
