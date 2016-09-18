import commands.AbstractCommandFactory;
import commands.CatCommand;
import commands.PwdCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Our shell.
 */
public final class ShellImpl implements Shell {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(ShellImpl.class.getName());
    @NotNull final Map<String, String> environment; // package-private for testing
    @NotNull private final AbstractCommandFactory commandFactory;

    public ShellImpl(@NotNull AbstractCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        registerShellCommands();

        environment = new HashMap<>();

        /*
          we are starting with `/` directory.
          As in `sh`, pwd is just a variable in my shell.
         */
        environment.put("pwd", "/");
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

    @Nullable
    private String getVariable(@NotNull String name) {
        return environment.getOrDefault(name, "");
    }

    private void setVariable(@NotNull String name, @NotNull String value) {
        environment.put(name, value);
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
        final Scanner scanner = new Scanner(System.in);
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
    private void processLine(@NotNull String line) throws ExecutionException, InvocationTargetException {
        line = parseLine(line); // tokenize first time and substitute variables
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
        final Token[] tokens = tokenizer.tokenize();

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

        StringBuilder resultLine = new StringBuilder();
        Arrays.stream(tokens).forEach(
                token -> {resultLine.append(token.printToken()); resultLine.append(" ");}
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
     * package-private for testing
     * @param token token of type ASSIGNMENT
     */
    void assignVariable(@NotNull Token token) {
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
          so I just tokenize string `"echo " + rawValue`, take
          second token (which should be an appropriate token for `rawValue`) and use already written function
          `substituteVariables` on it.
         */

        final Tokenizer tokenizer = new Tokenizer("echo " + rawValue);
        final Token tokenValue = tokenizer.tokenize()[1];
        substituteVariables(tokenValue);

        setVariable(variableName, tokenValue.getContent());
        token.changeContent(""); // we already processed all assignments
    }

    /**
     * evaluates the line.
     * @param line user's line after "tokenization" and substitution
     */
    private void evaluate(@NotNull String line) {
        final Tokenizer tokenizer = new Tokenizer(line);
        final Token[] tokens = tokenizer.tokenize();
    }

}
