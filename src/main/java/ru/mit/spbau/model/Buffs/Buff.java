package ru.mit.spbau.model.Buffs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.units.Attributes;

/**
 * This interface defines buffs in our game.
 *
 * Buff can be a real buff, or debuff
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
