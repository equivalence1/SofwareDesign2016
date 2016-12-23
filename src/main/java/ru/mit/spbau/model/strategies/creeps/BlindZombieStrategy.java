package ru.mit.spbau.model.strategies.creeps;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.units.creeps.CreepMove;

public final class BlindZombieStrategy implements CreepStrategy {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public CreepMove nextMove(@NotNull RelativeMap map) {
        switch ((int)Math.round(Math.random() * 4 - 0.5)) {
            case 0:
                return CreepMove.MOVE_UP;
            case 1:
                return CreepMove.MOVE_DOWN;
            case 2:
                return CreepMove.MOVE_LEFT;
            case 3:
                return CreepMove.MOVE_RIGHT;
        }
        return CreepMove.NO_MOVE;
    }

}
