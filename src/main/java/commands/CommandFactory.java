package commands;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by equi on 09.09.16.
 *
 * @author Kravchenko Dima
 */

/**
 * enum is a best way to make class singleton.
 */
public enum CommandFactory implements AbstractCommandFactory {

    INSTANCE;

    private Map<String, Class<?>> registeredCommands = new HashMap<>();

    public Command getCommand(String name) throws IllegalArgumentException, InvocationTargetException {
        if (registeredCommands.containsKey(name)) {
            return createInstance(registeredCommands.get(name));
        }

        throw new IllegalArgumentException("unknown command '" + name + "'.");
    }

    public boolean registerCommand(Class<?> clazz) throws IllegalArgumentException {
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

    private Command createInstance(Class<?> commandClass) throws InvocationTargetException {
        try {
            return (Command)commandClass.getConstructor().newInstance();
        } catch (NoSuchMethodException|InstantiationException|IllegalAccessException e) {
            /**
             * this is impossible. We do all this checks in {@link registeredCommands}
             */
        }
        return null; // this is also impossible
    }

    private boolean hasCommandAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(CommandAnnotation.class) != null;
    }

    private boolean hasPublicDefaultConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch((c) -> c.getParameterCount() == 0);
    }

    private boolean implementsCommand(Class<?> clazz) {
        return Command.class.isAssignableFrom(clazz);
    }

}
