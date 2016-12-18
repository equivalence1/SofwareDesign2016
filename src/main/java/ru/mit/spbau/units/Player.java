package ru.mit.spbau.units;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public final class Player extends PlayerUnit {

    @NotNull private final Queue<UserMove> q;

    public Player() {
        super(100);
        q = new LinkedBlockingDeque<>();
    }

    /**
     * Used by key listener to add user's last move
     * @param move user's last move
     */
    public void addMove(@NotNull UserMove move) {
        q.add(move);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public UserMove nextMove(@NotNull RelativeMap visibleMap) {
        return q.poll();
    }

}
