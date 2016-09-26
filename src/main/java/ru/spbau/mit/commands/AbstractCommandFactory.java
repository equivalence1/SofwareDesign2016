package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.Shell;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Interface for command factories
 */
public interface AbstractCommandFactory {

    /**
     * construct command by it's name
     * @param shell current shell
     * @param name name of command
     * @param out output stream for this command
     * @return command to be executed
     * @throws InvocationTargetException if command's constructor throws any exception
     */
    @NotNull
    Command getCommand(@NotNull Shell shell, @NotNull String name, @NotNull OutputStream out)
            throws InvocationTargetException;

    /**
     *
     * @param clazz class you want to register
     * @return whether your command
     * @throws IllegalArgumentException if your command does not comply with our requirements
     */
    boolean registerCommand(@NotNull Class<? extends Command> clazz) throws IllegalArgumentException;

}
