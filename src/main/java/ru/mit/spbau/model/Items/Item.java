package ru.mit.spbau.model.Items;

import oracle.jrockit.jfr.StringConstantPool;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.units.users.PlayerUnit;

/**
 * Defines some item which can be picked in our game.
 */
public abstract class Item extends GameObject {

    public Item(@NotNull Position position) {
        super(position);
    }

    public Item(@NotNull Item item) {
        super(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTransparent() {
        return true;
    }

    /**
     * Get class of this item
     * @return class of this item
     */
    public abstract ItemType getItemClass();

    /**
     * This item has been moved to player's inventory
     * so apply it's buffs to him.
     * Only players are allowed to wear some items now
     * (but it can be easily changed)
     * @param playerUnit player to buff
     */
    public abstract void pick(@NotNull PlayerUnit playerUnit);

    /**
     * Player dropped this item
     */
    public abstract void drop();

    /**
     * Every item should have some name
     * @return item's name
     */
    @NotNull
    public abstract String getName();

}
