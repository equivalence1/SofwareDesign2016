package ru.spbau.mit;

import ru.spbau.mit.commands.CommandFactory;

public final class Main {

    public static void main(String[] args) {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE,
                System.in, System.out);
        shell.run();
    }

}
