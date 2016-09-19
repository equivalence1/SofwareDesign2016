package ru.spbau.mit.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Each new command must be annotated with this annotation.
 * It should specify its name and it will be used in
 * {@link CommandFactory} do determine next command to be executed.
 * This annotation should be package-private
 * (It forces user to store all functions in `ru.spbau.mit.commands` package).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@interface CommandAnnotation {

    /**
     *
     * @return name of corresponding command
     */
    String commandName() default "";

}
