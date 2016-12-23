package ru.mit.spbau.model.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.game.Position;

/**
 * Defines a unit in our game.
 */
public abstract class Unit extends GameObject {

    @NotNull private final Attributes attributes;

    public Unit(@NotNull Position pos, @NotNull Attributes attributes) {
        super(pos);
        this.attributes = attributes;
    }

    protected Unit(@NotNull Unit unit) {
        super(unit);
        this.attributes = unit.attributes.copy();
    }

    /**
     * Get unit's attributes
     * @return unit's attributes
     */
    @NotNull
    public final Attributes getAttributes() {
        return this.attributes;
    }

    public final void move(Position.Direction direction) {
        getPosition().move(direction);
    }

}
