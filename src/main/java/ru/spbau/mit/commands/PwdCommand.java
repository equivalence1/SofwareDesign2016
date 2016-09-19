package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>pwd</code> command
 */
@CommandAnnotation(commandName = "pwd")
public final class PwdCommand extends Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(PwdCommand.class.getName());

    /**
     * {@inheritDoc}
     */
    public PwdCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    @Override
    public int execute(@NotNull InputStream in) {
        return execute(new Token[0]);
    }

    @Override
    public int execute(@NotNull Token[] args)  {
        try {
            out.write(shell.pwd().getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not write to output stream", e);
        }
        return 0;
    }

}
