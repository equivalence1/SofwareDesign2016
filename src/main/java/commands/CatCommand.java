package commands;

/**
 * Created by equi on 09.09.16.
 *
 * @author Kravchenko Dima
 */
@CommandAnnotation(commandName = "cat")
public class CatCommand implements Command {

    public int execute() {
        System.out.println("I am cat command");
        return 0;
    }

}
