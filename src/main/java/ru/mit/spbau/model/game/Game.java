package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.LevelMap;
import ru.mit.spbau.model.strategies.users.PlayerStrategy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of our logic.
 * Controls game flow.
 */
public final class Game {

    @NotNull private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private boolean shouldExit = false;
    @NotNull private final Thread gameThread;
    @NotNull private GameState gameState;
    @NotNull private final Player player;
    @NotNull private final PlayerStrategy playerStrategy;

    private int currentLevel;

    public Game(@NotNull PlayerStrategy playerStrategy, @NotNull String playerName) throws IOException {
        currentLevel = 0;
        gameThread = new Thread(this::gameLoop);
        this.playerStrategy = playerStrategy;
        player = new Player(playerName);
        nextLevel();
    }

    /**
     * Starts this game and it's main thread
     */
    public void start() {
        shouldExit = false;
        gameThread.start();
    }

    /**
     * stops this game
     */
    public void stop() {
        shouldExit = true;
        gameThread.interrupt();
    }

    @NotNull
    private LevelMap createMap(@NotNull String level, @NotNull PlayerStrategy playerStrategy)
            throws IOException {
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
            return LevelMap.fromFile(file, playerStrategy);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "could not find map for level '" + level + "'", e);
            throw e;
        }
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    private void gameLoop() {
        LOGGER.info("Starting game loop.");
        while (!shouldExit) {
            doOneMove();
            if (gameState.getGameStatus() == GameState.GameStatus.RUNNING) {
                continue;
            }
            if (gameState.getGameStatus() == GameState.GameStatus.LOSE) {
                player.getPlayerUnit().notifyLose(player.getName(), player.getScore());
                stop();
                break;
            }
            if (gameState.getGameStatus() == GameState.GameStatus.WIN) {
                if (isLastLevel()) {
                    player.getPlayerUnit().notifyWin(player.getName(), player.getScore());
                    break;
                } else {
                    try {
                        nextLevel();
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Could not construct next level");
                        player.getPlayerUnit().notifyWin(player.getName(), player.getScore());
                    }
                }
            }
        }
    }

    private boolean isLastLevel() {
        return currentLevel == 2;
    }

    private void nextLevel() throws IOException {
        currentLevel++;
        final LevelMap levelMap = createMap(String.valueOf(currentLevel), playerStrategy);
        gameState = new GameState(levelMap, this);
        player.setPlayerUnit(gameState.getMap().getPlayerUnit());
    }

    private void doOneMove() {
        gameState.doOneMove();
    }

}
