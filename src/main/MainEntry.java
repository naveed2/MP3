package main;

import membership.Proc;
import misc.MiscTool;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.text.Utilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MainEntry {

    private static Scanner in  = new Scanner(System.in);
    private static CommandMap commandMap = CommandMap.getInstance();
    private static Proc proc;
    private static Integer localPort;

    public static void main(String[] args) {

        log4jConfigure();
        init();
        work();

    }

    private static void log4jConfigure() {
        PropertyConfigurator.configure("log4j.properties");
        System.out.println("configure log4j successfully");
    }


    private static void init() {
        commandMap.initialize();

        printWelcomeMessage();
    }

    public static void work() {

        while(true) {
            String cmd = inputCommand();
            if(cmd.length() ==0) {
                continue;
            }

            String funcName = commandMap.findCommand(cmd);

            if(funcName == null) {
                //TODO: wrong command
            } else if(funcName.equals("quit")) {
                break;
            } else {
                MiscTool.callStaticMethod(MainEntry.class, funcName);
            }
        }

        System.out.println("Program quits");
    }

    private static void start() {
        localPort = MiscTool.inputPortNumber(in);
        proc = new Proc(localPort);
        proc.init();
    }

    private static void joinGroup() {

    }

    private static String inputCommand() {
        System.out.print(">");
        return in.nextLine();
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the fictitious Group-R-Us Inc.!");
        System.out.println("Author: Muhammad Naveed, Junjie Hu");
    }
}
