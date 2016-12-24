package ru.mit.spbau.model;

/**
 * This interface defines objects which could be coped.
 *
 * For now, in my game the main purpose of this interface is
 * to be able to pass our game object to strategies and be sure
 * that they wont be able cheat in any way, so mainly security reasons
 */
public interface Copyable {

    /**
     * make a copy of object
     * @return copy of object
     */
    Copyable copy();

}
