package ru.spbau.mit.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;
import ru.spbau.mit.parsing.Token;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public final class CdCommandTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        temporaryFolder.create();
        temporaryFolder.newFolder("subfolder1");
        temporaryFolder.newFolder("subfolder2");
    }

    @Test
    public void testCdRelative() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        shell.changeDirectory(temporaryFolder.getRoot().getAbsolutePath());
        CdCommand cdCommand = new CdCommand(shell, out);

        assertEquals(0, cdCommand.execute(System.in, new Token[] {
                new Token(Token.TokenType.WORD, "subfolder1")
        }));
        assertEquals("", out.toString());
        assertEquals(new File(temporaryFolder.getRoot(), "subfolder1").getAbsolutePath(), shell.pwd());
    }

    @Test
    public void testCdAbsolute() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        CdCommand cdCommand = new CdCommand(shell, out);

        assertEquals(0, cdCommand.execute(System.in, new Token[] {
                new Token(Token.TokenType.WORD, temporaryFolder.getRoot().getAbsolutePath())
        }));
        assertEquals("", out.toString());
        assertEquals(temporaryFolder.getRoot().getAbsolutePath(), shell.pwd());
    }

    @Test
    public void testCdParent() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        shell.changeDirectory(new File(temporaryFolder.getRoot(), "subfolder1").getAbsolutePath());
        CdCommand cdCommand = new CdCommand(shell, out);

        assertEquals(0, cdCommand.execute(System.in, new Token[] {
                new Token(Token.TokenType.WORD, "..")
        }));
        assertEquals("", out.toString());
        assertEquals(temporaryFolder.getRoot().getAbsolutePath(), shell.pwd());
    }
}