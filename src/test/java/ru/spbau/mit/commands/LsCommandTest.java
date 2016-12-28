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
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public final class LsCommandTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        temporaryFolder.create();
        temporaryFolder.newFile("a.txt");
        temporaryFolder.newFile("z.txt");
        File subfolder1 = temporaryFolder.newFolder("subfolder1");
        File subfolder2 = temporaryFolder.newFolder("subfolder2");
        new File(subfolder1, "b.txt").createNewFile();
        new File(subfolder1, "bz").mkdir();
        new File(subfolder1, "c.txt").createNewFile();

        new File(subfolder2, "d.txt").createNewFile();
        new File(subfolder2, "dz").mkdir();
        new File(subfolder2, "e.txt").createNewFile();
    }

    @Test
    public void testLsRelative() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        shell.changeDirectory(temporaryFolder.getRoot().getAbsolutePath());
        LsCommand lsCommand = new LsCommand(shell, out);

        assertEquals(0, lsCommand.execute(System.in, new Token[] {
                new Token(Token.TokenType.WORD, "subfolder1")
        }));
        String[] output = out.toString().split("\n");
        Arrays.sort(output);
        assertArrayEquals(new String[] { "b.txt", "bz", "c.txt" }, output);
    }

    @Test
    public void testLsAbsolute() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        LsCommand lsCommand = new LsCommand(shell, out);

        assertEquals(0, lsCommand.execute(System.in, new Token[] {
                new Token(Token.TokenType.WORD, new File(temporaryFolder.getRoot(), "subfolder2").getAbsolutePath())
        }));
        String[] output = out.toString().split("\n");
        Arrays.sort(output);
        assertArrayEquals(new String[] { "d.txt", "dz", "e.txt" }, output);
    }

    @Test
    public void testLsCurrentDir() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        shell.changeDirectory(temporaryFolder.getRoot().getAbsolutePath());
        LsCommand lsCommand = new LsCommand(shell, out);

        assertEquals(0, lsCommand.execute(System.in, new Token[] {}));
        String[] output = out.toString().split("\n");
        Arrays.sort(output);
        assertArrayEquals(new String[] { "a.txt", "subfolder1", "subfolder2", "z.txt" }, output);
    }
}