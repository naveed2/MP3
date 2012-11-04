package main;

import org.apache.log4j.PropertyConfigurator;

import java.util.Scanner;

public class MainEntry {

    private static Scanner in  = new Scanner(System.in);
    private static CommandMap commandMap = CommandMap.getInstance();

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
            }
        }

        System.out.println("Program quits");
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
