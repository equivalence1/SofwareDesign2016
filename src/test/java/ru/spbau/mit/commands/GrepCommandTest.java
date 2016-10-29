package ru.spbau.mit.commands;

import org.junit.Test;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;
import ru.spbau.mit.parsing.Token;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;

public class GrepCommandTest {

    @Test
    public void testGrep() throws Exception {
        final File temp = File.createTempFile("grep_sample1_file", ".tmp");
        final FileOutputStream fileContent = new FileOutputStream(temp);

        // final String sample1 = "cat " + temp.getAbsolutePath();
        final String content = "This test\nis some test_which\nwe w1ll use to\n\n\ntEst\nTEST\n\ngrep";

        fileContent.write(content.getBytes());
        fileContent.close();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        GrepCommand grepCommand = new GrepCommand(shell, out);

        assertEquals(0, grepCommand.execute(System.in, new Token[] {
                new Token(Token.TokenType.WORD, "-w"),
                new Token(Token.TokenType.WORD, "-i"),
                new Token(Token.TokenType.WORD, "-A 2"),
                new Token(Token.TokenType.WORD, "test"),
                new Token(Token.TokenType.WORD, temp.getAbsolutePath())
        }));
        assertEquals("This test\nis some test_which\ntEst\nTEST\n\n", out.toString());
    }

}