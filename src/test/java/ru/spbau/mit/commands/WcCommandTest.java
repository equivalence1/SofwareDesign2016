package ru.spbau.mit.commands;

import org.junit.Test;
import ru.spbau.mit.Shell;
import ru.spbau.mit.ShellImpl;

import java.io.*;

import static org.junit.Assert.assertEquals;

public final class WcCommandTest {

    @Test
    public void testWcStream() {
        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final WcCommand wc = new WcCommand(shell, out);

        final String sample = "bla\n bla   blabla\n\nbla ";
        final String answer = "4 4 " + sample.getBytes().length + " total\n";
        final ByteArrayInputStream in = new ByteArrayInputStream(sample.getBytes());

        assertEquals(0, wc.execute(in, ""));
        assertEquals(answer, out.toString());
    }

    @Test
    public void testWcFiles() throws IOException {
        final File temp1 = File.createTempFile("wc_sample1_file", ".tmp");
        final File temp2 = File.createTempFile("wc_sample2_file", ".tmp");
        final FileOutputStream fileContent = new FileOutputStream(temp1);

        final String content1 = "\n\nsome stup     \nid tex\nt";

        fileContent.write(content1.getBytes());
        fileContent.close();

        final Shell shell = new ShellImpl(CommandFactory.INSTANCE, System.in, System.out);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final WcCommand wc = new WcCommand(shell, out);

        final String answer = "5 5 " + content1.getBytes().length + " " + temp1.getAbsolutePath() + "\n" +
                "0 0 0 " + temp2.getAbsolutePath() + "\n5 5 " + content1.getBytes().length + " total\n";

        assertEquals(0, wc.execute(System.in, temp1.getAbsolutePath() +  " " + temp2.getAbsolutePath()));
        assertEquals(answer, out.toString());
    }

}