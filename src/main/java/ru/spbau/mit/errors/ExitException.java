package ru.spbau.mit.errors;

/**
 * This is a special kind of exception made for
 * <code>exit</code> command.
 *
 * As I handle all exception in the topmost loop
 * (which is {@link ru.spbau.mit.Shell#run}) then
 * <code>exit</code> command can just throw this
 * exception.
 *
 * Using this approach we dont need to
 * handle exit command somehow differently in our
 * parsers.
 */
public final class ExitException extends RuntimeException {
}
