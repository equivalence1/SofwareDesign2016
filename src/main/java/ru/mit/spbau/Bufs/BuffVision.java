package ru.mit.spbau.Bufs;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.units.Attributes;

/**
 * Change unit's vision range
 */
public final class BuffVision implements Buff {

    private final double visionRangeChagne;

    public BuffVision(double visionRangeChagne) {
        this.visionRangeChagne = visionRangeChagne;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(@NotNull Attributes attributes) {
        attributes.setVisionRange(attributes.getVisionRange() + visionRangeChagne);
    }

    @Override
    public void remove(@NotNull Attributes attributes) {
        attributes.setVisionRange(attributes.getVisionRange() - visionRangeChagne);
    }

}
