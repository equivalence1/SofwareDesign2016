package ru.mit.spbau.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;

/**
 * Player should implement this interface in order
 * to have ability to move
 *
 * Actually, as for now we have only 1 type of players.
 * this interface is excessive here but it may be convenient
 * in future (e.g. for some multi-player games and different game modes)
 */
public interface MoveablePlayer {

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
