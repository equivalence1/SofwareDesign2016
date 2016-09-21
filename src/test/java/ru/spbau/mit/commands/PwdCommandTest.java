package ru.spbau.mit.commands;

import org.junit.Test;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public final class PwdCommandTest {

    @Test
    public void testPwd() {
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        final URL location = PwdCommandTest.class.getProtectionDomain().getCodeSource().getLocation();
        shell.changeDirectory(location.getPath());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PwdCommand pwd = new PwdCommand(shell, out);

        assertEquals(0, pwd.execute(System.in, ""));
        assertEquals(location.getPath(), out.toString());
    }

}