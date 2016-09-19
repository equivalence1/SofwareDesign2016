package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.File;
import java.io.IOException;
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
     * {@inheritDoc}
     * @throws IllegalArgumentException if some token is not WORD or STRING
     */
    @Override
    public int execute(@NotNull Token[] args) throws IllegalArgumentException {
        for (Token arg : args) {
            int res;
            switch (arg.getTokenType()) {
                case WORD:
                case DOUBLE_QUOTED_STRING:
                case SINGLE_QUOTED_STRING:
                    res = writeFile(arg.getContent());
                    break;
                default:
                    throw new IllegalArgumentException("argument should be WORD|STRING");
            }
            if (res != 0) {
                return res;
            }
        }

        return 0;
    }

    /**
     * Executing <code>cat</code> command if argument is a file.
     * @param fileName name of file
     * @return exit code
     */
    private int writeFile(@NotNull String fileName) {
        final String filePath;
        if (fileName.length() > 0 && fileName.charAt(0) == '/') {
            filePath = fileName;
        } else {
            filePath = shell.pwd() + "/" + fileName;
        }
        final File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            LOGGER.log(Level.WARNING, "file `" + filePath + "` not found.");
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
            LOGGER.log(Level.WARNING, "error while reading file `" + filePath + "`.", e);
            return 1;
        }

        return 0;
    }

}
