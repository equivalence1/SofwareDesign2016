package ru.spbau.mit.commands;

import org.junit.Test;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public final class EchoCommandTest {

    @Test
    public void testEcho() {
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final EchoCommand echo = new EchoCommand(shell, out);

        final String command1 = "echo 123 ' 123' \"1 2 3\"";
        final String answer1 = "echo 123  123 1 2 3";

        assertEquals(0, echo.execute(System.in, command1));
        assertEquals(answer1, out.toString());
    }

}