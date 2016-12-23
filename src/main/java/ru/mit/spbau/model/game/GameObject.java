package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Copyable;

/**
 * All object lying on map should extend this class.
 * Basically it only contains object's position and
 * flag if this object is a copy of a real one.
 */
public abstract class GameObject implements Copyable {

    @NotNull private Position pos;
    private boolean isCopy = false;

    public GameObject(@NotNull Position pos) {
        this.pos = pos;
    }

    protected GameObject(@NotNull GameObject gameObject) {
        this.pos = gameObject.pos.copy();
        this.isCopy = true;
    }

    public final Position getPosition() {
        return this.pos;
    }

    public final void setPosition(@NotNull Position pos) {
        this.pos = pos;
    }

    /**
     * @return true if this is just a copy of real object on map, false otherwise
     */
    public final boolean isCopy() {
        return isCopy;
    }

    /**
     * @return true if other GameObjects (Units, actually) can stand on this object
     */
    public abstract boolean isTransparent();

    /**
     * deep-copy this object
     */
    public abstract GameObject copy();

}
