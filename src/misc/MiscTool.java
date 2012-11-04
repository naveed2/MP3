package misc;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MiscTool {

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
}
