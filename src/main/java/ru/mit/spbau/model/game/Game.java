package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.strategies.users.PlayerStrategy;

import java.util.logging.Logger;

/**
 * Main class of our logic.
 * Controls game flow and tightens all components together:
 * it keeps track for our units, our state and our map
 */
public final class Game {

    @NotNull private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

//    @NotNull private final Player player;
//    @NotNull private final MapGenerator mapGenerator;

    public Game(@NotNull PlayerStrategy playerStrategy) {

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

}
