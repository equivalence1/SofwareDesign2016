package ru.spbau.mit.commands;

import org.junit.Test;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;
import ru.spbau.mit.parsing.Token;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CatCommandTest {

    @Test
    public void testExecuteWithFile() throws IOException {
        final File temp = File.createTempFile("cat_sample1_file", ".tmp");
        final FileOutputStream fileContent = new FileOutputStream(temp);

        // final String sample1 = "cat " + temp.getAbsolutePath();
        final String answer1 = "some stupid text";

        fileContent.write(answer1.getBytes());
        fileContent.close();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        CatCommand catCommand = new CatCommand(shell, out);

        assertEquals(0, catCommand.execute(new Token[] {
                new Token(Token.TokenType.WORD, temp.getAbsolutePath()),
                new Token(Token.TokenType.DOUBLE_QUOTED_STRING, temp.getAbsolutePath()),
                new Token(Token.TokenType.SINGLE_QUOTED_STRING, temp.getAbsolutePath())
        }));
        assertEquals(answer1 + answer1 + answer1, out.toString());
    }

}