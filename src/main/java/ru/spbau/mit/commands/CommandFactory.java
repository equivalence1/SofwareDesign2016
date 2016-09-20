package ru.spbau.mit.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.mit.Shell;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * enum is a best way to make class singleton.
 */
public enum CommandFactory implements AbstractCommandFactory {

    INSTANCE;

    private final Map<String, Class<? extends Command>> registeredCommands = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Command getCommand(@NotNull Shell shell, @NotNull String name, @NotNull OutputStream out)
            throws InvocationTargetException {
        Command command = null;
        if (registeredCommands.containsKey(name)) {
            command = createInstance(shell, registeredCommands.get(name), out);
        }

        if (command != null) {
            return command;
        } else {
            return new OuterShellCommand(name, shell, out);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean registerCommand(@NotNull Class<? extends Command> clazz) throws IllegalArgumentException {
        if (hasCommandAnnotation(clazz)
                && hasProperConstructor(clazz)) {
            CommandAnnotation annotation = clazz.getAnnotation(CommandAnnotation.class);
            if (!annotation.commandName().isEmpty()) {
                registeredCommands.put(annotation.commandName(), clazz);
                return true;
            }
        }

        throw new IllegalArgumentException(clazz.getName() + " does not seem to be a valid command");
    }

    @Nullable
    private Command createInstance(@NotNull Shell shell, @NotNull Class<? extends Command> commandClass,
                                   @NotNull OutputStream out) throws InvocationTargetException {
        try {
            return commandClass.getConstructor(Shell.class, OutputStream.class).newInstance(shell, out);
        } catch (NoSuchMethodException|InstantiationException|IllegalAccessException e) {
            return null;
        }
    }

    private boolean hasCommandAnnotation(@NotNull Class<? extends Command> clazz) {
        return clazz.getAnnotation(CommandAnnotation.class) != null;
    }

    private boolean hasProperConstructor(@NotNull Class<? extends Command> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch(this::isConstructorProper);
    }

    private boolean isConstructorProper(Constructor<?> constructor) {
        return constructor.getParameterCount() == 2
                && Shell.class.isAssignableFrom(constructor.getParameterTypes()[0])
                && constructor.getParameterTypes()[1] == OutputStream.class;
    }

}
