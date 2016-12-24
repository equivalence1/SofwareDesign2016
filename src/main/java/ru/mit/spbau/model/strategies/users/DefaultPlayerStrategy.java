package ru.mit.spbau.model.strategies.users;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.units.users.UserMove;
import ru.mit.spbau.view.GUI;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public final class DefaultPlayerStrategy implements PlayerStrategy {

    @NotNull private final GUI gui;
    @NotNull private final BlockingQueue<UserMove> moves;


    public DefaultPlayerStrategy(@NotNull GUI gui) {
        this.gui = gui;
        moves = new LinkedBlockingDeque<>();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public UserMove nextMove(@NotNull RelativeMap relativeMap, int score) {
        gui.drawMap(relativeMap, score);
        try {
            return moves.take();
        } catch (InterruptedException e) {
            return UserMove.UNKNOWN;
        }
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

    /**
     * add user's move to a queue
     * @param move move to add
     */
    public void addMove(UserMove move) {
        moves.add(move);
    }

}
