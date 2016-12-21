package ru.mit.spbau.units;

import org.jetbrains.annotations.NotNull;

public final class Position {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
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

    public final int getX() {
        return x;
    }

    public final void setX(int x) {
        this.x = x;
    }

    public final int getY() {
        return y;
    }

    public final void setY(int y) {
        this.y = y;
    }

    public final void moveToDirection(@NotNull Direction d) {
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
        }
    }

    @NotNull
    public Position copy() {
        return new Position(x, y);
    }

}
