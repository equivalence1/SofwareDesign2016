package ru.mit.spbau.view.screens;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.ViewManager;

public final class StartScreen implements Screen {

    @NotNull private static final Logger LOGGER = Logger.getLogger(StartScreen.class.getName());

    @Override
    public void displayOutput(@NotNull AsciiPanel terminal) {
        LOGGER.log(Level.INFO, "displaying start screen");
        terminal.write("Roguelike", 1, 1);
        terminal.writeCenter("-- press [enter] to start or [esc] to exit --", ViewManager.getTerminalHeight() - 1);
    }

    @Override
    public Screen respondToUserInput(@NotNull KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                try {
                    return new PlayScreen();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Could not show play screen", e);
                    return null;
                }
            case KeyEvent.VK_ESCAPE:
                return null;
            default:
                return this;
        }
    }

}