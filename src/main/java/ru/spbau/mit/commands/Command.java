package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;
import ru.spbau.mit.parsing.Tokenizer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Each new shell command must extend this class.
 * This interface should be package-private
 * (It forces user to store all functions in `ru.spbau.mit.commands` package).
 *
 * Commands execution should not throw any checked exceptions.
 * They should use loggers to write about any kind of errors.
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
    public int execute(@NotNull InputStream in, @NotNull String args) {
        final Tokenizer tokenizer = new Tokenizer(args);
        return execute(in, tokenizer.tokenizeArgs());
    }

    /**
     * Execute command with "tokenized" arguments
     * @param args command arguments string
     * @return exit code
     */
    public abstract int execute(@NotNull InputStream in, @NotNull Token[] args);

}
