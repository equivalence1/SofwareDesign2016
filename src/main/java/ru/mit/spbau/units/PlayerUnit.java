package ru.mit.spbau.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.Game;
import ru.mit.spbau.map.RelativeMap;

/**
 * Defines unit controlled by player.
 */
public abstract class PlayerUnit extends Unit {

    @NotNull private final Game game;

    public PlayerUnit(@NotNull Game game, @NotNull Position pos, int maxHp) {
        super(pos, maxHp);
        this.game = game;
    }

    /**
     * Gets player's next move to perform next move providing
     * him with map as he sees it
     *
     * @param relativeMap map as this player sees it
     * @return player's next move
     */
    @NotNull
    public abstract UserMove nextMove(@NotNull RelativeMap relativeMap);

    @Override
    public final void notifyDead() {
        game.stop();
    }

}
