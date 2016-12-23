package ru.mit.spbau.model.units.creeps;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.strategies.creeps.BlindZombieStrategy;
import ru.mit.spbau.model.units.Attributes;

/**
 * Just some stupid creep
 */
public final class BlindZombieCreep extends CreepUnit {

    public BlindZombieCreep(@NotNull Position pos) {
        super(pos, new BlindZombieStrategy(), new Attributes(30, 5.0, 7));
    }

    private BlindZombieCreep(@NotNull BlindZombieCreep blindZombieCreep) {
        super(blindZombieCreep);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public BlindZombieCreep copy() {
        return new BlindZombieCreep(this);
    }

}
