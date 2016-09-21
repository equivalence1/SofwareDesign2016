package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.commands.*;
import ru.spbau.mit.parsing.Token;
import ru.spbau.mit.parsing.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Our shell.
 * You should it with CommandFactory instance, stdin and stdout.
 *
 * If you wish to add some new commands, you should implement it in
 * `commands` package (please, see already existing for examples)
 * and then you should register it in factory inside
 * {@link ShellImpl#registerShellCommands} method.
 */
public final class ShellImpl implements Shell {

    @NotNull private static final Logger LOGGER = Logger.getLogger(ShellImpl.class.getName());

    @NotNull private final Map<String, String> environment; // package-private for testing
    @NotNull private final AbstractCommandFactory commandFactory;

    @NotNull private final InputStream stdin;
    @NotNull private final OutputStream stdout;

    @NotNull private String currentDirectory;

    public ShellImpl(@NotNull AbstractCommandFactory commandFactory,
                     @NotNull InputStream stdin,
                     @NotNull OutputStream stdout) {
        this.commandFactory = commandFactory;
        this.stdin = stdin;
        this.stdout = stdout;

        registerShellCommands();

        currentDirectory = "~";
        environment = new HashMap<>();
    }

    /**
     * main method of the shell which will interpret user ru.spbau.mit.commands.
     */
    @Override
    public void run() {
        try {
            processLines();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "shell ru.spbau.mit.commands execution failed.", e);
            System.exit(1);
        }
    }

    @Override
    @NotNull
    public String getVariable(@NotNull String name) {
        return environment.getOrDefault(name, "");
    }

    @Override
    public void setVariable(@NotNull String name, @NotNull String value) {
        environment.put(name, value);
    }

    @Override
    @NotNull
    public String pwd() {
        return currentDirectory;
    }

    @Override
    public void changeDirectory(@NotNull String newDirectory) {
        File dir = new File(newDirectory);
        if (dir.exists() && dir.isDirectory()) {
            currentDirectory = newDirectory;
        } else {
            LOGGER.log(Level.WARNING, "directory `" + newDirectory + "` not found.");
        }
    }

    /**
     * registering all ru.spbau.mit.commands which this shell will recognize.
     */
    private void registerShellCommands() {
        /*
          May be it's better to do it with annotation processor, looking through
          `ru.spbau.mit.commands` folder, but I think it's overkill here.
         */
        commandFactory.registerCommand(CatCommand.class);
        commandFactory.registerCommand(PwdCommand.class);
        commandFactory.registerCommand(EchoCommand.class);
        commandFactory.registerCommand(WcCommand.class);
    }

    /**
     * reading lines from prompt
     */
    private void processLines() throws IOException {
        final Scanner scanner = new Scanner(stdin);
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
        stdout.close();
    }

    /**
     * processes next line from prompt.
     * package-private only for testing
     * @param line shell command to be processed
     */
    void processLine(@NotNull String line) throws RuntimeException, InvocationTargetException {
        line = parseLine(line); // tokenizeCommands first time and substitute variables
        evaluate(line);
    }

    /**
     * I make it package-private only for tests
     * @param line line you want to parse
     * @return line after "tokenization" + variables substitution
     */
    @NotNull
    String parseLine(@NotNull String line) {
        final Tokenizer tokenizer = new Tokenizer(line);
        final Token[] tokens = tokenizer.tokenizeCommands();

        for (Token token : tokens) {
            switch (token.getTokenType()) {
                case WORD:
                case DOUBLE_QUOTED_STRING:
                    substituteVariables(token);
                    break;
                case ASSIGNMENT:
                    assignVariable(token);
                    break;
            }
        }

        final StringBuilder resultLine = new StringBuilder();
        Arrays.stream(tokens).forEach(
                token -> {resultLine.append(token.getContentWithSurrounding()); resultLine.append(" ");}
        );
        return resultLine.toString();
    }

    /**
     * substitute variable inside token.
     * This method package-private only for testing
     * @param token token in which content we should substitute variables
     */
    void substituteVariables(@NotNull Token token) {
        if (token.getTokenType() != Token.TokenType.WORD
                && token.getTokenType() != Token.TokenType.DOUBLE_QUOTED_STRING) {
            return; // no need to substitute anything
        }

        final String content = token.getContent();
        final StringBuilder newContent = new StringBuilder();

        final Pattern variablePattern = Pattern.compile("\\$" + Token.VARIABLE_PATTERN);
        final Matcher variableMatcher = variablePattern.matcher(content);

        int lastEnd = 0;
        while (variableMatcher.find()) {
            newContent.append(content.substring(lastEnd, variableMatcher.start()));
            String variableName = variableMatcher.group().substring(1); // throw away '$' sign
            newContent.append(getVariable(variableName));
            lastEnd = variableMatcher.end();
        }
        newContent.append(content.substring(lastEnd));

        token.changeContent(newContent.toString());
    }

    /**
     * process ASSIGN token
     * @param token token of type ASSIGNMENT
     */
    private void assignVariable(@NotNull Token token) {
        if (token.getTokenType() != Token.TokenType.ASSIGNMENT) {
            throw new IllegalArgumentException("this operation only for ASSIGNMENT tokens");
        }

        final String content = token.getContent();
        int eqSign = content.indexOf('=');

        final String variableName = content.substring(0, eqSign);
        final String rawValue = content.substring(eqSign + 1);

        /*
         This is a pretty dirty trick.
         The value to the right of `=` sign can be some string with variable itself, e.g.
          > X=123
          > Y="$X" (or just Y=$X)
          > echo $Y
          should be `123`
          I don't want to write one more parser only for this cases,
          so I just tokenizeCommands string `"echo " + rawValue`, take
          second token (which should be an appropriate token for `rawValue`) and use already written function
          `substituteVariables` on it.
         */

        final Tokenizer tokenizer = new Tokenizer("echo " + rawValue);
        final Token tokenValue = tokenizer.tokenizeCommands()[1];
        substituteVariables(tokenValue);

        setVariable(variableName, tokenValue.getContent());
        token.changeContent(""); // we already processed all assignments
    }

    /**
     * evaluates the line after `parseLine`.
     * @param line user's line after "tokenization" and substitution (which are `parseLine` method)
     */
    private void evaluate(@NotNull String line) throws RuntimeException {
        final Tokenizer tokenizer = new Tokenizer(line);
        final Token[] tokens = tokenizer.tokenizeCommands();

        int currentPos = 0;
        int res = 0;

        OutputStream outputStream;
        Pipe prevPipe = null;
        Pipe currentPipe = null;

        while (currentPos < tokens.length) {
            final Token currentToken = tokens[currentPos];
            if (currentToken.getTokenType() == Token.TokenType.COMMAND) {
                final String commandName = currentToken.getContent();

                final StringBuilder args = new StringBuilder();
                int pos = currentPos + 1;
                while (pos < tokens.length && isArgument(tokens[pos])) {
                    args.append(tokens[pos].getContentWithSurrounding());
                    args.append(" ");
                    pos++;
                }

                if (pos != tokens.length && tokens[pos].getTokenType() != Token.TokenType.PIPE) {
                    /* After command's args we met something different than pipe. */
                    throw new RuntimeException("Could not evaluate line. Expected pipe or EOF after command.");
                }

                if (pos == tokens.length) {
                    /* this was the last command, so we just redirect it's output to stdout */
                    outputStream = stdout;
                } else {
                    /* we met pipe after this command, so redirect command's output to it */
                    currentPipe = new Pipe();
                    outputStream = currentPipe.getPipeInput();
                }

                final InputStream in;
                if (prevPipe != null) {
                    in = prevPipe.getPipeOutput();
                } else {
                    in = stdin;
                }
                res = executeCommand(commandName, in, args.toString(), outputStream);

                currentPos = pos;
            }

            if (res != 0) {
                return;
            }

            prevPipe = currentPipe;
            currentPipe = null;
            currentPos++; // skip pipe
        }
    }

    private int executeCommand(String commandName, InputStream in, String args, OutputStream out) {
        final Command command;
        try {
            command = commandFactory.getCommand(this, commandName, out);
            return command.execute(in, args);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, "could not execute command `" + commandName + "`", e.getTargetException());
            return 1;
        } catch (NullPointerException e) {
            LOGGER.log(Level.WARNING, "No command `" + commandName + "` found");
            return 1;
        }
    }

    private boolean isArgument(Token token) {
        switch (token.getTokenType()) {
            case DOUBLE_QUOTED_STRING:
            case SINGLE_QUOTED_STRING:
            case WORD:
                return true;
            default:
                return false;
        }
    }

}
