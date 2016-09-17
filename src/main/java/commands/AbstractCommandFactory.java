package commands;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Interface for command factories
 */
public interface AbstractCommandFactory {

    /**
     * construct command by it's name
     * @param name name of command
     * @return command to be executed
     * @throws IllegalArgumentException if your name does not comply to any known command
     * @throws InvocationTargetException if command's constructor throws any exception
     */
    Command getCommand(@NotNull String name) throws IllegalArgumentException, InvocationTargetException;

    /**
     *
     * @param clazz class you want to register
     * @return whether your command
     * @throws IllegalArgumentException
     */
    boolean registerCommand(@NotNull Class<?> clazz) throws IllegalArgumentException;

}
