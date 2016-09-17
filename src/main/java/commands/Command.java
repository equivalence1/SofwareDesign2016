package commands;

/**
 * Each new shell command must realize this interface.
 * This interface should be package-private
 * (It forces user to store all functions in `commands` package).
 */
interface Command {

    int execute();

    //void execute(inPipe, outStream);

    //void execute(List<something> args, outStream);

    //void help(outStream);

}
