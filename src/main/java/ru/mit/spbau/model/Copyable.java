package ru.mit.spbau.model;

/**
 * This interface defines objects which could be coped.
 *
 * For now, in my game the main purpose of this interface is
 * to be able to pass our game object to strategies and be sure
 * that they wont be able cheat in any way.
 *
 * Even though creating this interface I only had security reasons
 * in my head, one may find it helpful for other ones.
 *
 * Yes, I know that this interface is pretty dumb as every object
 * in Java has <code>copy</code> method but with this one I wanted
 * to emphasise that some class should override <code>copy</code>in
 * security reasons.
 */
public interface Copyable {

    /**
     * make a copy of object
     * @return copy of object
     */
    Copyable copy();

}
