package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.LevelMap;

/**
 * Stores all the information about current game
 * and performs one move
 */
public final class GameState {

    @NotNull private final Player player;
    @NotNull private final LevelMap map;
    @NotNull private final Game game;
    @NotNull private GameStatus gameStatus;

    public GameState(@NotNull LevelMap map, @NotNull String playerName, @NotNull Game game) {
        this.map = map;
        this.game = game;
        player = new Player(playerName);
        player.setPlayerUnit(map.getPlayerUnit());
        gameStatus = GameStatus.RUNNING;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public LevelMap getMap() {
        return map;
    }

    @NotNull
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void doOneMove() {
        map.doOneMove();
        if (!player.getPlayerUnit().isAlive()) {
            gameStatus = GameStatus.LOSE;
        }
        if (map.getAllCreeps().size() == 0) {
            gameStatus = GameStatus.WIN;
        }
    }

    public enum GameStatus {
        RUNNING,
        WIN,
        LOSE
    }

}
