package ru.mit.spbau.model.Items;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Buffs.Buff;
import ru.mit.spbau.model.Buffs.BuffMaxHp;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.units.users.PlayerUnit;

/**
 * Just simple armor
 */
public final class SimpleArmor extends Item {

    private final int defence = 5;
    @NotNull private final Buff hpBuff;
    private PlayerUnit playerUnit;

    public SimpleArmor(@NotNull Position position) {
        super(position);
        hpBuff = new BuffMaxHp(defence);
    }

    private SimpleArmor(@NotNull SimpleArmor armor) {
        super(armor);
        this.hpBuff = new BuffMaxHp(defence);
        this.playerUnit = null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ItemType getItemClass() {
        return ItemType.ARMOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pick(@NotNull PlayerUnit playerUnit) {
        this.playerUnit = playerUnit;
        playerUnit.getAttributes().applyBuff(hpBuff);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop() {
        if (playerUnit != null) {
            playerUnit.getAttributes().removeBuff(hpBuff);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Simple Armor";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleArmor copy() {
        return new SimpleArmor(this);
    }

}
