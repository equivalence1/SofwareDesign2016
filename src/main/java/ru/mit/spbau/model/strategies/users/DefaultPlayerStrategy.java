package ru.mit.spbau.model.strategies.users;

import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.units.users.UserMove;
import ru.mit.spbau.view.GUI;

public final class DefaultPlayerStrategy implements PlayerStrategy {

    @NotNull private final GUI gui;

    public DefaultPlayerStrategy(@NotNull GUI gui) {
        this.gui = gui;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public UserMove nextMove(@NotNull RelativeMap relativeMap) {
        gui.drawMap(relativeMap);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyWin(@NotNull String name, int score) {
        gui.notifyWin(name, score);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyLose(@NotNull String name, int score) {
        gui.notifyLose(name, score);
    }

}
