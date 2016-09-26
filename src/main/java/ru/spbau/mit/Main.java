package ru.spbau.mit;

import ru.spbau.mit.commands.CommandFactory;

/**
 * Just main class
 */
public final class Main {

    /**
     * Just main function
     */
    public static void main(String[] args) {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE,
                System.in, System.out);
        shell.run();
    }

}
