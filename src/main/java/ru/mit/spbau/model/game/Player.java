package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.units.users.PlayerUnit;

/**
 * Stores main information about player
 */
public final class Player {

    @NotNull private final String name;
    private PlayerUnit playerUnit;
    private int score;

    public Player(@NotNull String name) {
        this.name = name;
        this.score = 0;
    }

    public void setPlayerUnit(@NotNull PlayerUnit playerUnit) {
        this.playerUnit = playerUnit;
    }

    public PlayerUnit getPlayerUnit() {
        return playerUnit;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    /**
     * increment player's score
     * @param change how much to add to score
     */
    public void incScore(int change) {
        score += change;
    }

}
