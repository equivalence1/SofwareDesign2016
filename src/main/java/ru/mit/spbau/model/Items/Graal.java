package ru.mit.spbau.model.Items;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.units.users.PlayerUnit;

/**
 * The main purpose of player is to take graal
 */
public final class Graal extends Item {

    private PlayerUnit playerUnit;

    public Graal(@NotNull Position position) {
        super(position);
    }

    private Graal(@NotNull Graal graal) {
        super(graal);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ItemType getItemClass() {
        return ItemType.GRAAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pick(@NotNull PlayerUnit playerUnit) {
        this.playerUnit = playerUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graal copy() {
        return new Graal(this);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "The saint graal";
    }

}
