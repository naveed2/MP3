package misc;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MiscTool {

    public static final Integer BUFFER_SIZE = 4096;

    private static Logger logger = Logger.getLogger(MiscTool.class);

    public static void callStaticMethod(Class className, String methodName) {
        Method method = null;
        try {
            method = className.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(null);    //static method
            method.setAccessible(false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Integer inputPortNumber(Scanner in) {
        System.out.print("Input the port: ");
        String str = in.nextLine();
        int ret;
        while(true) {
            try {
                ret = Integer.parseInt(str);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Number format error");
            }
        }
        return ret;
    }

    public static String inputAddress(Scanner in) {
        String str;
        while(true) {
            System.out.println("Input the address:");
            str = in.nextLine();
            if(MiscTool.isIPAddress(str)) {
                break;
            }
        }
        return str;
    }

    public static boolean isIPAddress(String str) {
        String[] res = str.split(":");
        if(res.length!=2) {
            return false;
        }

        String ip;
        Integer port;
        try{
            port = Integer.parseInt(res[1]);
        } catch(NumberFormatException ex) {
            return false;
        }

        if(port<=0 || port >=65536) {
            return false;
        }

        ip = res[0];

        String regex;

        regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$" ;
        return ip.matches(regex);
    }
}
