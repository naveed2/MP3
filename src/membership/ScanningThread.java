package membership;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScanningThread {

    private Proc proc;
    private AtomicBoolean shouldStop;
    private static final Integer INTERVAL = 5000;
    private static final Integer MAX_TIME_DIFFERENCE = 100;

    public ScanningThread() {
        shouldStop = new AtomicBoolean(false);
    }

    public void setProc(Proc proc) {
        this.proc = proc;
    }

    public void startScan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!shouldStop.get()) {
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        //
                    }
                    proc.getMemberList().updateMemberList();
                }
            }
        }).start();
    }
}
