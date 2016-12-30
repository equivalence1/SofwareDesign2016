package ru.mit.spbau.model.strategies.creeps;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.units.creeps.CreepMove;

public interface CreepStrategy {

    /**
     * Asks unit to perform next move
     * @param relativeMap map as this creep sees it
     * @return unit's next move
     */
    @NotNull
    CreepMove nextMove(@NotNull RelativeMap relativeMap);

}
