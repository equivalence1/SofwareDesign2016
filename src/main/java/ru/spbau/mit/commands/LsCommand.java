package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>ls</code> command.
 */
@CommandAnnotation(commandName = "ls")
public class LsCommand extends Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(LsCommand.class.getName());

    /**
     * {@inheritDoc}
     */
    public LsCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    /**
     * execute <code>ls</code> command
     * @param in ignored
     * @param args the only argument should contain directory to list. If <code>args</code> is empty, current directory is listed.
     */
    @Override
    public int execute(@NotNull InputStream in, @NotNull Token[] args) {
        if (args.length > 1) {
            LOGGER.log(Level.WARNING, "Expected not more than 1 argument for ls, got " + args.length);
            return 1;
        }
        Path dir = Paths.get(args.length == 0 ? "" : args[0].getContent());
        if (!dir.isAbsolute()) {
            dir = Paths.get(shell.pwd()).resolve(dir);
        }
        try {
            for (Path p : Files.newDirectoryStream(dir)) {
                out.write((p.getFileName().toString() + "\n").getBytes());
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error while executing", e);
            return 1;
        }
        return 0;
    }
}
