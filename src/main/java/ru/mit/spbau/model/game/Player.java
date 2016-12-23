package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.units.users.PlayerUnit;

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

    public void setScore(int score) {
        this.score = score;
    }

    public void changeScore(int change) {
        score += change;
    }

}
