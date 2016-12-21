package ru.mit.spbau.Bufs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.units.Attributes;

/**
 * This buff changes maximum hp of unit.
 */
public final class BuffMaxHp implements Buff {

    private final int maxHpChange;

    public BuffMaxHp(int maxHpChange) {
        this.maxHpChange = maxHpChange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(@NotNull Attributes attributes) {
        attributes.setMaxHp(attributes.getMaxHp() + maxHpChange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull Attributes attributes) {
        attributes.setMaxHp(attributes.getMaxHp() - maxHpChange);
    }

}
