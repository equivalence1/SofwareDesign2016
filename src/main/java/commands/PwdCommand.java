package commands;

/**
 * Created by equi on 14.09.16.
 *
 * @author Kravchenko Dima
 */
@CommandAnnotation(commandName = "pwd")
public class PwdCommand implements Command {

    public int execute() {
        System.out.print("/:");
        return 0;
    }

}
