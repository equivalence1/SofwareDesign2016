package ru.mit.spbau.model.Buffs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.units.Attributes;

public class BuffCurrentHp implements Buff {

    private final int currentHpChange;

    public BuffCurrentHp(int currentHpChange) {
        this.currentHpChange = currentHpChange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(@NotNull Attributes attributes) {
        attributes.setCurrentHp(Math.min(attributes.getCurrentHp() + currentHpChange, attributes.getMaxHp()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull Attributes attributes) {
        attributes.setCurrentHp(Math.max(0, attributes.getCurrentHp() - currentHpChange));
    }


}
