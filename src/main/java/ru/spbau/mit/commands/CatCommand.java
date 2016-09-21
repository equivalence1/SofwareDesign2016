package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>cat</code> command
 */
@CommandAnnotation(commandName = "cat")
public final class CatCommand extends Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(CatCommand.class.getName());

    /**
     * {@inheritDoc}
     */
    public CatCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    /**
     * execute <code>cat</code> command
     * @param in if args are empty, cat will take it's input from here
     * @param args are considered as files' names
     */
    @Override
    public int execute(@NotNull InputStream in, @NotNull Token[] args) {
        /*
         echo /path/to/file | cat
         should produce `/path/to/file`
         but
         cat /path/to/file
         should produce file's content

         So if args[] does have any tokens, than ignore `in`.
         Print content of `in` else.
         */

        if (args.length > 0) {
            return execute(args);
        } else {
            return execute(in);
        }
    }

    private int execute(@NotNull Token[] args) {
        for (Token arg : args) {
            int res;
            switch (arg.getTokenType()) {
                case WORD:
                case DOUBLE_QUOTED_STRING:
                case SINGLE_QUOTED_STRING:
                    res = readFile(arg.getContent());
                    break;
                default:
                    LOGGER.log(Level.WARNING, "argument should be WORD|STRING");
                    return 1;
            }
            if (res != 0) {
                return res;
            }
        }

        return 0;
    }

    private int execute(@NotNull InputStream in) {
        try {
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error while executing", e);
            return 1;
        }

        return 0;
    }

    /**
     * Executing <code>cat</code> command if argument is a file.
     * @param fileName name of file
     * @return exit code
     */
    private int readFile(@NotNull String fileName) {
        final String filePath;
        if (fileName.length() > 0 && fileName.charAt(0) == '/') {
            filePath = fileName;
        } else {
            filePath = shell.pwd() + "/" + fileName;
        }
        final File file = new File(filePath);

        if (!file.exists()) {
            LOGGER.log(Level.WARNING, "file `" + filePath + "` not found.");
            return 1;
        }

        if (file.isDirectory()) {
            LOGGER.log(Level.WARNING, "file `" + filePath + "` is a directory.");
            return 1;
        }

        if (!file.canRead()) {
            LOGGER.log(Level.WARNING, "can not read file `" + filePath + "`.");
            return 1;
        }

        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            out.write(data);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error while handling file `" + filePath + "`.", e);
            return 1;
        }

        return 0;
    }

}
