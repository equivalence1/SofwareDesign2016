package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for shells
 */
public interface Shell {

    void run();

    @NotNull
    String getVariable(@NotNull String name);

    void setVariable(@NotNull String name, @NotNull String value);

    /**
     * it's a shell's responsibility to keep trek on current working directory
     * @return current working directory
     */
    @NotNull
    String pwd();

    /**
     * change current directory
     * @param newDirectory full path to the new directory
     */
    void changeDirectory(@NotNull String newDirectory);

}
