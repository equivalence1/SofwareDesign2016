package ru.mit.spbau.view;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.RelativeMap;

public interface GUI {

    void drawMap(@NotNull RelativeMap relativeMap);

    /**
     * Tells our GUI that player won
     * @param name player's name
     * @param score player's score
     */
    void notifyWin(@NotNull String name, int score);

    /**
     * Tells our GUI that player lost
     * @param name player's name
     * @param score player's score
     */
    void notifyLose(@NotNull String name, int score);

}
