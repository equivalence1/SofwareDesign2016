package ru.mit.spbau.view.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Screen {

    /**
     * Display current ru.mit.spbau.view in given terminal
     * @param terminal terminal in which we will display ru.mit.spbau.view
     */
    void displayOutput(@NotNull AsciiPanel terminal);

    /**
     * Respond to user action and return new screen
     * @param key key which user pressed
     * @return new screen to display or null if user wants to exit
     */
    @Nullable
    Screen respondToUserInput(@NotNull KeyEvent key);

}
