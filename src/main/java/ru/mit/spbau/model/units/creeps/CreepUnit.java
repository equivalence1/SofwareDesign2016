package ru.mit.spbau.model.units.creeps;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.strategies.creeps.CreepStrategy;
import ru.mit.spbau.model.units.Attributes;
import ru.mit.spbau.model.units.Unit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines creep unit in our game.
 */
public abstract class CreepUnit extends Unit {

    @NotNull private static final Logger LOGGER = Logger.getLogger(CreepUnit.class.getName());

    private final CreepStrategy creepStrategy; // can be null in copy

    public CreepUnit(@NotNull Position pos, CreepStrategy creepStrategy, @NotNull Attributes attributes) {
        super(pos, attributes);
        this.creepStrategy = creepStrategy;
    }

    protected CreepUnit(@NotNull CreepUnit creepUnit) {
        super(creepUnit);
        this.creepStrategy = null;
    }

    /**
     * Make a single move
     * @param relativeMap map as this unit sees it
     * @return next move
     */
    @NotNull
    public CreepMove nextMove(@NotNull RelativeMap relativeMap) {
        try {
            return creepStrategy.nextMove(relativeMap);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception occurred in player's strategy.", e);
            return CreepMove.NO_MOVE;
        }
    }

}
