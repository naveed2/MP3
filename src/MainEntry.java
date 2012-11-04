import org.apache.log4j.PropertyConfigurator;

public class MainEntry {
    public static void main(String[] args) {

        log4jConfigure();

        System.out.println("test");
    }

    private static void log4jConfigure() {
        PropertyConfigurator.configure("log4j.properties");
    }
}
