package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Items.ItemType;
import ru.mit.spbau.model.map.LevelMap;

/**
 * Stores all the information about current game
 * and performs one move
 */
public final class GameState {

    @NotNull private final LevelMap map;
    @NotNull private final Player player;
    @NotNull private GameStatus gameStatus;

    public GameState(@NotNull LevelMap map, @NotNull Player player) {
        this.map = map;
        this.player = player;
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

    /**
     * Perform one move in a game
     */
    public void doOneMove() {
        map.doOneMove(this);
        if (!player.getPlayerUnit().isAlive()) {
            gameStatus = GameStatus.LOSE;
        }
        if (getPlayer().getPlayerUnit().getInventory().contains(ItemType.GRAAL)) {
            gameStatus = GameStatus.WIN;
        }
    }

    public enum GameStatus {
        RUNNING,
        WIN,
        LOSE
    }

}
