package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class OuterShellCommand extends Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(OuterShellCommand.class.getName());

    @NotNull private final String name;

    public OuterShellCommand(@NotNull String name, @NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
        this.name = name;
    }

    @Override
    public int execute(@NotNull InputStream in, @NotNull Token[] args) {
        final String[] processArgs = new String[args.length + 1];
        processArgs[0] = name;
        int i = 1;

        for (Token arg : args) {
            processArgs[i++] = arg.getContent();
        }

        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(processArgs);
            final Process process = processBuilder.start();
            final OutputStream processInput = process.getOutputStream();

            int byteRead;
            final byte[] buffer = new byte[1024];
            while ((byteRead = in.read(buffer)) != -1) {
                processInput.write(buffer, 0, byteRead);
            }
            processInput.close();

            int exitCode = process.waitFor();

            final InputStream processOutput = process.getInputStream();
            while ((byteRead = processOutput.read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }

            return exitCode;
        } catch (IOException|InterruptedException e) {
            LOGGER.log(Level.WARNING, "command `" + name + "` was executed like outer command and failed.", e);
            return 1;
        }
    }

}
