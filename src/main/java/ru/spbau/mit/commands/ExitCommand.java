package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.errors.ExitException;
import ru.spbau.mit.parsing.Token;

import java.io.InputStream;
import java.io.OutputStream;

@CommandAnnotation(commandName = "exit")
public class ExitCommand extends Command {

    public ExitCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    /**
     * Finishes shell by throwing {@link ExitException}
     */
    public int execute(@NotNull InputStream in, @NotNull String args) throws ExitException {
        throw new ExitException();
    }

    /**
     * Finishes shell by throwing {@link ExitException}
     */
    public int execute(@NotNull InputStream in, @NotNull Token[] args) throws ExitException {
        throw new ExitException();
    }

}
