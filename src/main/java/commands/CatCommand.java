package commands;

/**
 * <code>cat</code> command
 */
@CommandAnnotation(commandName = "cat")
public final class CatCommand implements Command {

    public int execute() {
        System.out.println("I am cat command");
        return 0;
    }

}
