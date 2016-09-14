import commands.AbstractCommandFactory;
import commands.CatCommand;
import commands.PwdCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by equi on 08.09.16.
 *
 * @author Kravchenko Dima
 */

/**
 * Our shell.
 */
public class ShellImpl implements Shell {

    private static final Logger LOGGER = Logger.getLogger(ShellImpl.class.getName());
    private AbstractCommandFactory commandFactory;

    public ShellImpl(AbstractCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        registerShellCommands();
    }

    /**
     * main method of the shell which will interpret user commands.
     */
    public void run() {
        try {
            processLines();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "shell commands execution failed.", e);
            System.exit(1);
        }
    }

    /**
     * registering all commands which this shell will recognize.
     */
    private void registerShellCommands() {
        commandFactory.registerCommand(CatCommand.class);
        commandFactory.registerCommand(PwdCommand.class);
    }

    /**
     * reading lines from prompt
     */
    private void processLines() {
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while (scanner.hasNextLine()) {
            try {
                line = scanner.nextLine();
                processLine(line);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "could not process line '" + line + "'.", e);
            }
        }

        scanner.close();
    }

    /**
     * processes next line from prompt
     * @param line shell command to be processed
     */
    private void processLine(String line) throws ExecutionException, InvocationTargetException {
        String[] tokens = line.split(" ");
        Arrays.stream(tokens).filter(this::isStringArg).map(this::substituteVariables);
        commandFactory.getCommand(tokens[0]).execute();
    }

    /**
     * checks it this token is a double-quoted string
     * @param token token you want to check
     * @return whether this token is a double-quoted string
     */
    private boolean isStringArg(String token) {
        return (token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"');
    }

    private String substituteVariables(String token) {
        return token;
    }

}
