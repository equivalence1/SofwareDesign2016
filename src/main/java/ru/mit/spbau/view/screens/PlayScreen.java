package ru.mit.spbau.view.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.Game;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.RelativeMap;
import ru.mit.spbau.units.UserMove;

public final class PlayScreen implements Screen {

    @NotNull private final Game game;

    public PlayScreen(@NotNull Game game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayOutput(@NotNull AsciiPanel terminal) {
        terminal.write("You are having fun.", 1, 1);
        terminal.writeCenter("-- press [escape] to exit game --", 22);
        drawMap(terminal, game.getPlayer().getMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Screen respondToUserInput(@NotNull KeyEvent key) {
        switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                game.stop();
                return new StartScreen();
            default:
                final UserMove userMove = getUserMove(key.getKeyCode());
                game.getPlayer().addMove(userMove);
                return this;
        }
    }

    private UserMove getUserMove(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                return UserMove.UP;
            case KeyEvent.VK_DOWN:
                return UserMove.DOWN;
            case KeyEvent.VK_RIGHT:
                return UserMove.RIGHT;
            case KeyEvent.VK_LEFT:
                return UserMove.LEFT;
            case KeyEvent.VK_E:
                return UserMove.PICK;
            default:
                return UserMove.UNKNOWN;
        }
    }

    /**
     * show map to user
     * @param terminal in which we will draw
     * @param map map as user sees it
     */
    private void drawMap(@NotNull AsciiPanel terminal, @Nullable RelativeMap map) {

    }

}
