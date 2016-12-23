package ru.mit.spbau.model.Buffs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.units.Attributes;

/**
 * Change unit's vision range
 */
public final class BuffVision implements Buff {

    private final double visionRangeChange;

    public BuffVision(double visionRangeChagne) {
        this.visionRangeChange = visionRangeChagne;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(@NotNull Attributes attributes) {
        attributes.setVisionRange(attributes.getVisionRange() + visionRangeChange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull Attributes attributes) {
        attributes.setVisionRange(attributes.getVisionRange() - visionRangeChange);
    }

}
