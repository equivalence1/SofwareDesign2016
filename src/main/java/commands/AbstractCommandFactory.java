package commands;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by equi on 14.09.16.
 *
 * @author Kravchenko Dima
 */
public interface AbstractCommandFactory {

    Command getCommand(String name) throws IllegalArgumentException, InvocationTargetException;

    boolean registerCommand(Class<?> clazz) throws IllegalArgumentException;

}
