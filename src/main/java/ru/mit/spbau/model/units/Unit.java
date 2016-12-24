package ru.mit.spbau.model.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.map.MapCell;

/**
 * Defines a unit in our game.
 */
public abstract class Unit extends GameObject {

    @NotNull private final Attributes attributes;
    private MapCell[][] knownMap;

    public Unit(@NotNull Position pos, @NotNull Attributes attributes) {
        super(pos);
        this.attributes = attributes.copy();
    }

    protected Unit(@NotNull Unit unit) {
        super(unit);
        this.attributes = unit.attributes.copy();
        if (unit.knownMap != null) {
            this.knownMap = new MapCell[unit.knownMap.length][unit.knownMap[0].length];
            for (int i = 0; i < knownMap.length; i++) {
                for (int j = 0; j < knownMap[i].length; j++) {
                    this.knownMap[i][j] = new MapCell(unit.knownMap[i][j].getTexture(), unit.knownMap[i][j].getVisability());
                }
            }
        }
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

    public final MapCell[][] getKnownMap() {
        return knownMap;
    }

    public final void setKnownMap(@NotNull MapCell[][] knownMap) {
        this.knownMap = knownMap;
    }

    /**
     * Attack this unit with some attack
     * @param attack strength with which it was attacked
     */
    public final void attacked(int attack) {
        attributes.setCurrentHp(Math.max(0, attributes.getCurrentHp() - attack));
    }

    /**
     * Check if this unit is alive
     * @return true if hp > 0, false otherwise
     */
    public final boolean isAlive() {
        return attributes.getCurrentHp() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTransparent() {
        return false;
    }

    /**
     * How much score will player get for killing this unit
     * @return cost of this unit
     */
    public abstract int getCost();

}
