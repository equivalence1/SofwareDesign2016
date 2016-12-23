package ru.mit.spbau.view.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;

public final class LoseScreen implements Screen {

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayOutput(@NotNull AsciiPanel terminal) {
        terminal.write("ru.mit.spbau.model.game.Game Over", 1, 1);
        terminal.writeCenter("-- press [enter] to go to menu --", 22);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Screen respondToUserInput(@NotNull KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
    }
}