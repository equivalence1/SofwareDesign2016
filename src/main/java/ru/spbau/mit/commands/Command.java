package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;
import ru.spbau.mit.parsing.Tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Each new shell command must extend this class.
 * This interface should be package-private
 * (It forces user to store all functions in `ru.spbau.mit.commands` package).
 */
public abstract class Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(Command.class.getName());

    @NotNull protected final Shell shell;
    @NotNull protected final OutputStream out;

    /**
     * Constructs command
     * @param shell current shell
     * @param out output stream for a command
     */
    public Command(@NotNull Shell shell, @NotNull OutputStream out) {
        this.shell = shell;
        this.out = out;
    }

    /**
     * Execute command with a string representation of arguments (more human-readable and easier to use)
     * @param args command arguments string
     * @return exit code
     */
    public int execute(@NotNull String args) {
        final Tokenizer tokenizer = new Tokenizer(args);
        return execute(tokenizer.tokenize());
    }

    /**
     * Execute command taking args from InputStream
     * @param inputStream ru.spbau.mit.commands input
     * @return exit code
     */
    public int execute(@NotNull InputStream inputStream) {
        final StringBuilder args = new StringBuilder();

        try {
            int byteRead = 0;
            final byte[] buffer = new byte[1024];
            while ((byteRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < byteRead; i++) {
                    args.append(buffer[i]);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not read arguments from input stream.", e);
            return 1;
        }

        return execute(args.toString());
    }

    /**
     * Execute command with "tokenized" arguments
     * @param args command arguments string
     * @return exit code
     */
    public abstract int execute(@NotNull Token[] args);

}
