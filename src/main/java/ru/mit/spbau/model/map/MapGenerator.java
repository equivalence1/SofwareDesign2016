package ru.mit.spbau.model.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MapGenerator {

    @NotNull private static final Logger LOGGER = Logger.getLogger(MapGenerator.class.getName());

    public MapGenerator() {

    }

    @Nullable
    public LevelMap getMap(@NotNull String level) {
        try {
            final ClassLoader classLoader = getClass().getClassLoader();
            final File file = new File(classLoader.getResource("lvl_" + level + ".map").getFile());
            if (!file.exists()) {
                throw new IllegalArgumentException("File with level does not exists.");
            }
            if (!file.isFile()) {
                throw new IllegalArgumentException("File for level is not a regular file.");
            }
            if (!file.canRead()) {
                throw new IllegalArgumentException("Can not read file for level.");
            }
            return LevelMap.fromFile(file);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "could not find map for level '" + level + "'", e);
            return null;
        }
    }

}
