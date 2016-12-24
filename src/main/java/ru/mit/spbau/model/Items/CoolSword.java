package ru.mit.spbau.model.Items;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Buffs.Buff;
import ru.mit.spbau.model.Buffs.BuffAttack;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.units.users.PlayerUnit;

/**
 * Just not so simple sword
 */
public final class CoolSword extends Item {

    private final int attack = 10;
    @NotNull private final Buff attackBuff;
    private PlayerUnit playerUnit;

    public CoolSword(@NotNull Position position) {
        super(position);
        attackBuff = new BuffAttack(attack);
    }

    private CoolSword(@NotNull CoolSword sword) {
        super(sword);
        this.attackBuff = new BuffAttack(attack);
        this.playerUnit = null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ItemType getItemClass() {
        return ItemType.WEAPON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pick(@NotNull PlayerUnit playerUnit) {
        this.playerUnit = playerUnit;
        playerUnit.getAttributes().applyBuff(attackBuff);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop() {
        if (playerUnit != null) {
            playerUnit.getAttributes().removeBuff(attackBuff);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoolSword copy() {
        return new CoolSword(this);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Cool sword";
    }

}
