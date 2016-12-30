package ru.mit.spbau.model.units.creeps;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.strategies.creeps.StupidZombieStrategy;
import ru.mit.spbau.model.units.Attributes;

/**
 * Just some stupid creep
 */
public final class ZombieCreep extends CreepUnit {

    public ZombieCreep(@NotNull Position pos) {
        super(pos, new StupidZombieStrategy(), new Attributes(30, 5.0, 7));
    }

    private ZombieCreep(@NotNull ZombieCreep blindZombieCreep) {
        super(blindZombieCreep);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ZombieCreep copy() {
        return new ZombieCreep(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCost() {
        return 3;
    }

}
