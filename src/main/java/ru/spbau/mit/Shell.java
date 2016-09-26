package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for shells
 */
public interface Shell {

    /**
     * The main method of shell which will interpret user's commands
     */
    void run();

    /**
     * Get environment variable
     * @param name name of variable
     * @return variable's value
     */
    @NotNull
    String getVariable(@NotNull String name);

    /**
     * Set environment variable
     * @param name name of variable
     * @param value new value of variable
     */
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
