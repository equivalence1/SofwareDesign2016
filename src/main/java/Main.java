import commands.CommandFactory;

/**
 * Created by equi on 14.09.16.
 *
 * @author Kravchenko Dima
 */
public class Main {

    public static void main(String[] args) {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE);
        shell.run();
    }

}
