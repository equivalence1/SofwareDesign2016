package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * ru.spbau.mit.Shell pipe.
 * In my implementation pipe is just a 64KB buffer.
 * You should use it like this:
 * Say we have sequence of command like this
 *
 * <code>echo "1" | cat</code>
 *
 * In this example you should give to <code>echo</code> command
 * pipe's input ({@link Pipe#getPipeInput}), write to it, and after
 * that give pipe's output ({@link Pipe#getPipeOutput}) as the arguments
 * to <code>cat</code> command (you probably should convert it to string)
 */
public class Pipe {

    @NotNull private final ByteArrayOutputStream outputStream;

    public Pipe() {
        outputStream = new ByteArrayOutputStream(64 * 1024); //64 KB
    }

    /**
     * ru.spbau.mit.Pipe's output is a command input stream
     * @return InputStream build out of outputStream
     */
    @NotNull
    public ByteArrayInputStream getPipeOutput() {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * ru.spbau.mit.Pipe's input is a command output stream
     * @return outputStream
     */
    @NotNull
    public ByteArrayOutputStream getPipeInput() {
        return outputStream;
    }

}
