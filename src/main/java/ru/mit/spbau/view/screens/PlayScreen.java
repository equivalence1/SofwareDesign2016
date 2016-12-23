package ru.mit.spbau.view.screens;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.model.game.Game;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.map.MapCell;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.strategies.users.DefaultPlayerStrategy;
import ru.mit.spbau.model.units.Unit;
import ru.mit.spbau.model.units.creeps.CreepMove;
import ru.mit.spbau.model.units.creeps.CreepUnit;
import ru.mit.spbau.model.units.users.PlayerUnit;
import ru.mit.spbau.model.units.users.UserMove;
import ru.mit.spbau.view.GUI;
import ru.mit.spbau.view.ViewManager;

public final class PlayScreen implements Screen, GUI {

    @NotNull private static final Logger LOGGER = Logger.getLogger(StartScreen.class.getName());

    @NotNull private final DefaultPlayerStrategy playerStrategy;
    @NotNull private final Game game;
    private RelativeMap map;

    public PlayScreen() throws IOException {
        playerStrategy = new DefaultPlayerStrategy(this);
        LOGGER.info("taking default strategy for user");
        this.game = new Game(playerStrategy, "I'm too lazy to prompt user for name");
        LOGGER.info("starting game");
        this.game.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayOutput(@NotNull AsciiPanel terminal) {
        LOGGER.log(Level.INFO, "Displaying play screen.");
        if (map == null) {
            terminal.write("Have fun!", 1, 1);
        } else {
            displayMap(terminal);
        }
        terminal.writeCenter("-- press [escape] to exit game --", 22);
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
                playerStrategy.addMove(userMove);
                return this;
        }
    }

    @Override
    public void notifyLose(@NotNull String userName, int score) {
        LOGGER.info("Notified lose.");
        ViewManager.getViewManager().setScreen(new LoseScreen());
    }

    @Override
    public void notifyWin(@NotNull String userName, int score) {
        LOGGER.info("Notified win.");
        ViewManager.getViewManager().setScreen(new WinScreen());
    }

    @Override
    public void drawMap(@NotNull RelativeMap map) {
        LOGGER.info("drawing new map");
        this.map = map;
        ViewManager.getViewManager().setScreen(this);
    }

    private void displayMap(@NotNull AsciiPanel terminal) {
        final int mapHeight = map.getMapHeight();
        final int mapWidth = map.getMapWidth();
        final MapCell[][] knownMap = map.getKnownMap();

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                writeCell(terminal, i, j, knownMap[i][j]);
            }
        }

        for (GameObject object : map.getObjects()) {
            displayGameObject(object, terminal);
        }
        displayGameObject(map.getSelf(), terminal);
    }

    private void displayGameObject(@NotNull GameObject object, @NotNull AsciiPanel terminal) {
        if (object instanceof CreepUnit) {
            terminal.write('Z', object.getPosition().getX(), object.getPosition().getY(), Color.RED);
        }
        if (object instanceof PlayerUnit) {
            terminal.write('@', object.getPosition().getX(), object.getPosition().getY(), Color.YELLOW);
        }
    }

    private void writeCell(@NotNull AsciiPanel terminal, int y, int x, @NotNull MapCell cell) {
        final char ch = getChar(cell);
        final Color foregroundColor = getForegroundColor(cell);
        final Color backgroundColor = getBackgroundColor(cell);
        terminal.write(ch, x, y, foregroundColor, backgroundColor);
    }

    private char getChar(@NotNull MapCell cell) {
        switch (cell.getTexture()) {
            case EMPTY:
                return '.';
            case UNKNOWN:
                return ' ';
            case WALL:
                return '#';
            case WATER:
                return '~';
            case EXIT:
                return 'X';
        }
        return ' ';
    }

    private Color getBackgroundColor(@NotNull MapCell cell) {
        switch (cell.getVisability()) {
            case VISIBLE:
                return Color.BLACK;
            case VISITED:
                return Color.GRAY;
            default:
                return Color.black;
        }
    }

    private Color getForegroundColor(@NotNull MapCell cell) {
        switch (cell.getTexture()) {
            case EMPTY:
                return Color.WHITE;
            case UNKNOWN:
                return Color.BLACK;
            case WALL:
                return Color.GRAY;
            case WATER:
                return Color.BLUE;
            case EXIT:
                return Color.GREEN;
            default:
                return Color.WHITE;
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

}
