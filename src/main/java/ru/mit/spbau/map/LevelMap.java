package ru.mit.spbau.map;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class carries our map and units
 */
public final class LevelMap {

    @NotNull private final MapCell[][] map;

    private LevelMap(int width, int height) {
        map = new MapCell[height][width];
    }

    /**
     * Read map from file
     * @param file containing map
     * @return map
     * @throws IOException in case of error
     */
    public static LevelMap fromFile(@NotNull File file) throws IOException {
        final Scanner scanner = new Scanner(file);

        int height = 0;
        int width = 0;

        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
        }
    }

}
