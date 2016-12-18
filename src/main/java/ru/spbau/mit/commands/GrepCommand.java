package ru.spbau.mit.commands;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <code>grep</code> command
 */
@CommandAnnotation(commandName = "grep")
public final class GrepCommand extends Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(GrepCommand.class.getName());

    private Options options;
    private Pattern pattern;
    private int linesToPrint = 1;
    private CommandLine cmd;

    public GrepCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    /**
     * {@inheritDoc}
     */
    public int execute(@NotNull InputStream in, @NotNull String args) {
        makeOptions();
        final CommandLineParser parser = new GnuParser();
        try {
            cmd = parser.parse(options, args.split(" "), true);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "could not parse grep arguments", e);
            return 1;
        }

        if (cmd.getArgs().length == 0) {
            LOGGER.log(Level.WARNING, "You should provide grep with pattern");
            return 1;
        }
        preparePattern(cmd.getArgs()[0]);

        if (cmd.getArgs().length > 1) {
            return execute(Arrays.copyOfRange(cmd.getArgs(), 1, cmd.getArgs().length)); // grep working with files
        } else {
            return execute(in); // working with input stream
        }
    }

    /**
     * {@inheritDoc}
     */
    public int execute(@NotNull InputStream in, @NotNull Token[] args) {
        final String stringArgs = Arrays.asList(args)
                .stream()
                .map(Token::getContentWithSurrounding).collect(Collectors.joining(" "));
        return execute(in, stringArgs);
    }

    private void makeOptions() {
        options = new Options();

        options.addOption("i", false, " Ignore case distinctions in both the PATTERN and the input files.");
        options.addOption("w", false, "Select only those lines containing matches that form whole words.");
        options.addOption(OptionBuilder
                .withArgName("NUM")
                .hasArg()
                .withDescription("Print NUM lines of trailing context after matching lines.")
                .create("A")
        );
    }

    private void preparePattern(String sPattern) {
        int flags = 0;
        if (cmd.hasOption("i")) {
            flags |= Pattern.CASE_INSENSITIVE;
        }

        if (cmd.hasOption("w")) {
            pattern = Pattern.compile("(\\b|^)" + sPattern + "(\\b|$)", flags);
        } else {
            pattern = Pattern.compile(sPattern, flags);
        }

        if (cmd.hasOption("A")) {
            linesToPrint = NumberUtils.toInt(cmd.getOptionValue("A"), 1);
        }
    }

    private int execute(String[] fileNames) {
        for (String fileName : fileNames) {
            final byte[] emptyArr = new byte[0];
            final ByteArrayInputStream catIn = new ByteArrayInputStream(emptyArr);
            final ByteArrayOutputStream catOut = new ByteArrayOutputStream();
            int ret = makeCat(catIn, fileName, catOut);
            if (ret != 0) {
                return ret;
            }

            final String content = catOut.toString();

            ret = execute(content);
            if (ret != 0) {
                return ret;
            }
        }

        return 0;
    }

    private int execute(InputStream in) {
        final ByteArrayOutputStream catOut = new ByteArrayOutputStream();
        int ret = makeCat(in, "", catOut);
        if (ret != 0) {
            return ret;
        }

        final String content = catOut.toString();

        return execute(content);
    }

    private int execute(@NotNull String content) {
        final String[] lines = content.split("\n");
        int needPrint = 0;
        for (String line : lines) {
            final Matcher m = pattern.matcher(line);
            if (m.find()) {
                needPrint = linesToPrint;
            }
            if (needPrint > 0) {
                needPrint -= 1;
                try {
                    out.write((line + "\n").getBytes());
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "could not print line", e);
                    return 1;
                }
            }
        }
        return 0;
    }

    private int makeCat(@NotNull InputStream in, @NotNull String args, @NotNull OutputStream out) {
        final CatCommand cat = new CatCommand(shell, out);
        return cat.execute(in, args);
    }

}
