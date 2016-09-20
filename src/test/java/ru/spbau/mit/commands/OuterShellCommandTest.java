package ru.spbau.mit.commands;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by equi on 20.09.16.
 *
 * @author Kravchenko Dima
 */
public class OuterShellCommandTest {

    private Shell shell;

    @Before
    public void initShell() {
        shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
    }

    @Test
    public void testOuterEcho() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        final OuterShellCommand command = new OuterShellCommand("echo", shell, out);

        assertEquals(0, command.execute(in, "123 \"123\" '123'"));
        assertEquals("123 123 123\n", out.toString());
    }

    @Test
    public void testOuterCat() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ByteArrayInputStream in = new ByteArrayInputStream("echo 123 \"2+2\"".getBytes());
        final OuterShellCommand command = new OuterShellCommand("cat", shell, out);

        assertEquals(0, command.execute(in, ""));
        assertEquals("echo 123 \"2+2\"", out.toString());
    }

}