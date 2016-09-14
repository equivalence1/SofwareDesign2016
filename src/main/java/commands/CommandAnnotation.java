package commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by equi on 09.09.16.
 *
 * @author Kravchenko Dima
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CommandAnnotation {
    /**
     * Each new command should be annotated with this annotation.
     * It should specify its name and it will be used in
     * {@link CommandFactory} do determine next command to be executed.
     *
     * @return name of corresponding command
     */
    String commandName() default "";
}
