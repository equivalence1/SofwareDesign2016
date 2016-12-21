package ru.mit.spbau.Bufs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.units.Attributes;

/**
 * This interface defines buffs in our game.
 */
public interface Buff {

    /**
     * This buff gets applied to a given set of attributes
     * @param attributes attributes of some unit
     */
    void apply(@NotNull Attributes attributes);

    /**
     * This buff gets removed from a given set of attributes
     * @param attributes attributes of some unit
     */
    void remove(@NotNull Attributes attributes);

}
