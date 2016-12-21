package ru.mit.spbau.Bufs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.units.Attributes;

/**
 * This buff changes attack of unit.
 */
public final class BuffAttack implements Buff {

    private final int attackChange;

    public BuffAttack(int attackChange) {
        this.attackChange = attackChange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(@NotNull Attributes attributes) {
        attributes.setAttack(attributes.getAttack() + attackChange);
    }

    @Override
    public void remove(@NotNull Attributes attributes) {
        attributes.setAttack(attributes.getAttack() - attackChange);
    }

}
