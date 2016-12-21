package ru.mit.spbau.view.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.game.Game;

public final class StartScreen implements Screen {

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayOutput(@NotNull AsciiPanel terminal) {
        terminal.write("Roguelike", 1, 1);
        terminal.writeCenter("-- press [enter] to start or [esc] to exit --", 22);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Screen respondToUserInput(@NotNull KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                final Game game = new Game();
                return new PlayScreen(game);
            case KeyEvent.VK_ESCAPE:
                return null;
            default:
                return this;
        }
    }

}
