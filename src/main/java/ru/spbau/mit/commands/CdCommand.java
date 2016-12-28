package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>cd</code> command.
 */
@CommandAnnotation(commandName = "cd")
public final class CdCommand extends Command {
    @NotNull
    private static final Logger LOGGER = Logger.getLogger(CdCommand.class.getName());

    /**
     * {@inheritDoc}
     */
    public CdCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    /**
     * execute <code>cd</code> command
     * @param in ignored
     * @param args arg should contain a single item - directory to cd to
     */
    @Override
    public int execute(@NotNull InputStream in, @NotNull Token[] args) {
        if (args.length != 1) {
            LOGGER.log(Level.WARNING, "cd expected exactly one argument, got " + args.length);
            return 1;
        }
        Path path = Paths.get(args[0].getContent());
        if (path.isAbsolute()) {
            shell.changeDirectory(path.normalize().toString());
        } else {
            shell.changeDirectory(Paths.get(shell.pwd()).resolve(path).normalize().toString());
        }
        return 0;
    }
}
