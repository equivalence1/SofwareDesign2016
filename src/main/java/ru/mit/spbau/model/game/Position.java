package ru.mit.spbau.model.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.Copyable;

/**
 * Just to store positions of our units
 */
public final class Position implements Copyable {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        HOLD
    }

    private int x;
    private int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** get x */
    public final int getX() {
        return x;
    }

    /** set x */
    public final void setX(int x) {
        this.x = x;
    }

    /** get y */
    public final int getY() {
        return y;
    }

    /** set y */
    public final void setY(int y) {
        this.y = y;
    }

    /**
     * Move one position in a given direction.
     * I still leave setters in case we want to have some kind of teleportation
     * @param d direction to move
     * @return this position after move
     */
    public final Position move(@NotNull Direction d) {
        switch (d) {
            case UP:
                y -= 1;
                break;
            case DOWN:
                y += 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
            case HOLD:
                break;
        }
        return this;
    }

    /**
     * get distance between two positions
     * @param position position to which to count distance to
     * @return distance
     */
    public double getDistance(@NotNull Position position) {
        return Math.sqrt((x - position.x) * (x - position.x) + (y - position.y) * (y - position.y));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Position copy() {
        return new Position(x, y);
    }

    /**
     * Check if positions are equal
     * @param pos position with which to compare
     * @return true if positions are equal, false otherwise
     */
    public boolean equals(Position pos) {
        return x == pos.x && y == pos.y;
    }

}
