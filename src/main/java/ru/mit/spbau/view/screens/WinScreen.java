package ru.mit.spbau.view.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.ViewManager;

public final class WinScreen implements Screen {

    @NotNull private final String name;
    private final int score;

    public WinScreen(@NotNull String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayOutput(@NotNull AsciiPanel terminal) {
        terminal.write(name + " won!", 1, 1);
        terminal.write("Your score: " + score, 1, 2);
        terminal.writeCenter("-- press [enter] to go to menu. --", ViewManager.getTerminalHeight() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Screen respondToUserInput(@NotNull KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
    }
}

