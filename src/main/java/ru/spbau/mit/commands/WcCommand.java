package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;
import ru.spbau.mit.parsing.Token;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>wc</code> command
 */
@CommandAnnotation(commandName = "wc")
public final class WcCommand extends Command {

    @NotNull private static final Logger LOGGER = Logger.getLogger(WcCommand.class.getName());

    private int totalBytesCount;
    private int totalLinesCount;
    private int totalWordsCount;

    private int currentBytesCount;
    private int currentLinesCount;
    private int currentWordsCount;

    private byte lastByte;

    public WcCommand(@NotNull Shell shell, @NotNull OutputStream out) {
        super(shell, out);
    }

    @Override
    public int execute(@NotNull InputStream in, @NotNull Token[] args) {
        /*
          That's what real wc does. If it has some args, it will only work with them (as files)
          If does not, then it will work with input stream
         */
        totalBytesCount = 0;
        totalLinesCount = 0;
        totalWordsCount = 0;

        if (args.length != 0) {
            return execute(args);
        } else {
            return execute(in);
        }
    }

    private int execute(@NotNull Token[] args) {
        int res;

        for (Token arg : args) {
            res = readFile(arg.getContent());
            if (res != 0) {
                return res;
            }
        }

        if (args.length > 1) {
            try {
                final String totalResult = getTotalResults();

                out.write(totalResult.getBytes());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "error while writing to output stream.", e);
                return 1;
            }
        }

        return 0;
    }

    private int execute(@NotNull InputStream in) {
        try {
            int byteRead;
            final byte[] buffer = new byte[1024];
            while ((byteRead = in.read(buffer)) != -1) {
                handleBytes(buffer, byteRead);
            }

            final String totalResults = getTotalResults();
            out.write(totalResults.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error while writing/reading to/from output/input stream.", e);
            return 1;
        }

        return 0;
    }

    /**
     * Executing <code>wc</code> command if argument is a file.
     * @param fileName name of file
     * @return exit code
     */
    private int readFile(@NotNull String fileName) {
        final String filePath;
        if (fileName.length() > 0 && fileName.charAt(0) == '/') {
            filePath = fileName;
        } else {
            filePath = shell.pwd() + "/" + fileName;
        }
        final File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            LOGGER.log(Level.WARNING, "file `" + filePath + "` not found.");
            return 1;
        }

        if (!file.canRead()) {
            LOGGER.log(Level.WARNING, "can not read file `" + filePath + "`.");
            return 1;
        }

        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);

            lastByte = ' ';
            handleBytes(data, data.length);

            String fileResult = currentLinesCount + " " + currentWordsCount + " "
                    + currentBytesCount + " " + fileName + "\n";

            out.write(fileResult.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error while handing file `" + filePath + "`.", e);
            return 1;
        }

        return 0;
    }

    private void handleBytes(@NotNull byte[] data, int size) {
        currentBytesCount = size;
        currentLinesCount = 0;
        currentWordsCount = 0;

        byte curByte;

        for (int i = 0; i < size; i++) {
            curByte = data[i];

            if (isWordBreak(curByte) && !isWordBreak(lastByte)) {
                currentWordsCount += 1;
            }

            if (curByte == '\n') {
                currentLinesCount += 1;
            }

            lastByte = curByte;
        }

        if (!isWordBreak(lastByte) && currentBytesCount != 0) {
            currentWordsCount += 1;
        }

        if (lastByte != '\n' && currentBytesCount != 0) {
            currentLinesCount += 1;
        }

        totalBytesCount += currentBytesCount;
        totalWordsCount += currentWordsCount;
        totalLinesCount += currentLinesCount;
    }

    private boolean isWordBreak(byte b) {
        return b == ' ' || b == '\n' || b == '\t' || b == '\r';
    }

    @NotNull
    private String getTotalResults() {
        return totalLinesCount + " " + totalWordsCount + " " + totalBytesCount + " total\n";
    }

}
