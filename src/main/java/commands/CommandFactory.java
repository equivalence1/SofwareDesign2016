package commands;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * enum is a best way to make class singleton.
 */
public enum CommandFactory implements AbstractCommandFactory {

    INSTANCE;

    private Map<String, Class<?>> registeredCommands = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    public Command getCommand(@NotNull String name) throws IllegalArgumentException, InvocationTargetException {
        if (registeredCommands.containsKey(name)) {
            return createInstance(registeredCommands.get(name));
        }

        throw new IllegalArgumentException("unknown command '" + name + "'.");
    }

    /**
     * {@inheritDoc}
     */
    public boolean registerCommand(@NotNull Class<?> clazz) throws IllegalArgumentException {
        if (hasCommandAnnotation(clazz)
                && hasPublicDefaultConstructor(clazz)
                && implementsCommand(clazz)) {
            CommandAnnotation annotation = clazz.getAnnotation(CommandAnnotation.class);
            if (!annotation.commandName().isEmpty()) {
                registeredCommands.put(annotation.commandName(), clazz);
                return true;
            }
        }

        throw new IllegalArgumentException(clazz.getName() + " does not seem to be a valid command");
    }

    private Command createInstance(@NotNull Class<?> commandClass) throws InvocationTargetException {
        try {
            return (Command)commandClass.getConstructor().newInstance();
        } catch (NoSuchMethodException|InstantiationException|IllegalAccessException e) {
            /**
             * this is impossible. We do all this checks in {@link registeredCommands}
             */
        }
        return null; // this is also impossible
    }

    private boolean hasCommandAnnotation(@NotNull Class<?> clazz) {
        return clazz.getAnnotation(CommandAnnotation.class) != null;
    }

    private boolean hasPublicDefaultConstructor(@NotNull Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch((c) -> c.getParameterCount() == 0);
    }

    private boolean implementsCommand(@NotNull Class<?> clazz) {
        return Command.class.isAssignableFrom(clazz);
    }

}
