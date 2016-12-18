package ru.mit.spbau.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;

/**
 * All creeps should implement this interface
 * in order to have ability to move
 */
public interface MoveableCreep {

    /**
     * Asks unit to perform next move
     * @param relativeMap map as this creep sees it
     * @return unit's next move
     */
    CreepMove nextMove(@NotNull RelativeMap relativeMap);

}
