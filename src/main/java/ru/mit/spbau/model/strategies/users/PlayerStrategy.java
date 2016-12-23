package ru.mit.spbau.model.strategies.users;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.units.users.UserMove;

/**
 * Common interface for player strategies
 */
public interface PlayerStrategy {

    /**
     * Asks unit to perform next move.
     * @param relativeMap part as this player sees it
     * @return unit's next move
     */
    @NotNull
    UserMove nextMove(@NotNull RelativeMap relativeMap);

    /**
     * Notify this strategy that it lost
     * @param name name of player
     * @param score player's score
     */
    void notifyLose(@NotNull String name, int score);

    /**
     * Notify this strategy that it won
     * @param name name of player
     * @param score player's score
     */
    void notifyWin(@NotNull String name, int score);

}
