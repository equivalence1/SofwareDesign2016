package ru.mit.spbau.model.units.users;

public enum UserMove {
    UP,
    DOWN,
    RIGHT,
    LEFT,
    PICK,
    NO_MOVE, // in single-user mode, actually, we don't need this kind of move
    UNKNOWN
}
