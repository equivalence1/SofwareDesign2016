package commands;

/**
 * <code>pwd</code> command
 */
@CommandAnnotation(commandName = "pwd")
public final class PwdCommand implements Command {

    public int execute() {
        System.out.print("/:");
        return 0;
    }

}
