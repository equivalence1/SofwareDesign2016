package ru.mit.spbau.strategies;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;
import ru.mit.spbau.units.CreepMove;

public interface CreepStrategy {

    /**
     * Asks unit to perform next move
     * @param relativeMap map as this creep sees it
     * @return unit's next move
     */
    CreepMove nextMove(@NotNull RelativeMap relativeMap);

}
