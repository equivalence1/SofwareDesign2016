package ru.mit.spbau.strategies;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;
import ru.mit.spbau.units.UserMove;

public interface PlayerStrategy {

    /**
     * Asks unit to perform next move.
     *
     * For now (again, as we have only 1 player which can see screen)
     * we dont need this <code>MapPart visibleMap</code> param
     * But in future, if we wonted to create some bots, it could
     * be helpful.
     *
     * @param relativeMap part as this player sees it
     * @return unit's next move
     */
    UserMove nextMove(@NotNull RelativeMap relativeMap);

}
