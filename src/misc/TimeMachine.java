package misc;

public class TimeMachine {

    private Long baseTime;

    private static TimeMachine instance = new TimeMachine();

    private TimeMachine() {
        baseTime = (long) 0;
    }

    public static void init() {
        instance.baseTime = System.currentTimeMillis();
    }

    public static Long getTime() {
        return (System.currentTimeMillis() - instance.baseTime) / 100;
    }

}
