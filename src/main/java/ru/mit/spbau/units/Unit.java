package ru.mit.spbau.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;

/**
 * Defines a unit in our game.
 */
public abstract class Unit {

    @NotNull private final Position pos;
    @NotNull private final Attributes attributes;

    public Unit(@NotNull Position pos, @NotNull Attributes attributes) {
        this.pos = pos;
        this.attributes = attributes;
    }

    public final Position getPosition() {
        return this.pos;
    }

    public final int getMaxHp() {
        return maxHp;
    }

    public final int getCurrentHp() {
        return currentHp;
    }

    public final void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    /**
     * Just notification to unit that he is dead.
     * Unit can ignore it but he will be removed from game anyway.
     */
    public abstract void notifyDead();

}
