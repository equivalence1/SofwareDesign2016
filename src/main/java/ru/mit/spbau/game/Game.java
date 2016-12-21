package ru.mit.spbau.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.map.LevelMap;
import ru.mit.spbau.map.MapGenerator;
import ru.mit.spbau.map.RelativeMap;
import ru.mit.spbau.units.Player;
import ru.mit.spbau.units.Unit;

import java.util.logging.Logger;

/**
 * Main class of our logic.
 * Controls game flow and tightens all components together:
 * it keeps track for our units, our state and our map
 */
public final class Game {

    @NotNull private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    @NotNull private final Player player;
    @NotNull private final MapGenerator mapGenerator;

    public Game() {
        player = new Player();
        mapGenerator = new MapGenerator();
    }

    /**
     * Starts this game and it's main thread
     */
    public void start() {

    }

    /**
     * stops this game
     */
    public void stop() {

    }

    public void doOneMove() {

    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public RelativeMap getRelativeMap(@NotNull Unit unit) {
        return null;
    }

    @Nullable
    private LevelMap getMap(String level) {
        return mapGenerator.getMap(level);
    }

}
