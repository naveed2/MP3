package main;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {

    private static CommandMap instance = new CommandMap();
    private Map<String, String> stringToFuncName;

    private static final String CMD_START = "start";
    private static final String FUNC_START = "start";

    private static final String CMD_HELP = "help";
    private static final String FUNC_HELP = "printHelp";

    private static final String CMD_QUIT = "quit";
    private static final String FUNC_QUIT ="quit";

    private CommandMap() {

    }

    public static CommandMap getInstance() {
        return instance;
    }

    public synchronized CommandMap initialize() {
        instance = new CommandMap();
        stringToFuncName = new HashMap<String, String>();

        stringToFuncName.put(CMD_START, FUNC_START);
        stringToFuncName.put(CMD_HELP, FUNC_HELP);
        stringToFuncName.put(CMD_QUIT, FUNC_QUIT);

        return this;
    }

    private static final String FORMAT_STRING = "%-25s%-25s\n";

    public static void printHelp() {
        System.out.printf(FORMAT_STRING, "COMMAND", "USAGE");
        System.out.printf(FORMAT_STRING, CMD_QUIT, "quit");
    }

    public synchronized String findCommand(String cmd) {
        return stringToFuncName.get(cmd);
    }
}
