package ru.mit.spbau.model.Items;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Buffs.Buff;
import ru.mit.spbau.model.Buffs.BuffCurrentHp;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.units.users.PlayerUnit;

public final class HealingPotion extends Item {

    private final int heal = 10;
    @NotNull
    private final Buff healBuff;
    private PlayerUnit playerUnit;

    public HealingPotion(@NotNull Position position) {
        super(position);
        healBuff = new BuffCurrentHp(heal);
    }

    private HealingPotion(@NotNull HealingPotion potion) {
        super(potion);
        this.healBuff = new BuffCurrentHp(heal);
        this.playerUnit = null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ItemType getItemClass() {
        return ItemType.INSTA_USE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pick(@NotNull PlayerUnit playerUnit) {
        this.playerUnit = playerUnit;
        playerUnit.getAttributes().applyBuff(healBuff);
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
    public HealingPotion copy() {
        return new HealingPotion(this);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Healing potion";
    }

}
