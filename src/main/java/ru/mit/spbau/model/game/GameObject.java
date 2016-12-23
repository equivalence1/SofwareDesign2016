package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Copyable;

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

    public final boolean isCopy() {
        return isCopy;
    }

    public abstract GameObject copy();

}
