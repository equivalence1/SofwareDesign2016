package ru.mit.spbau.view.screens;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import asciiPanel.AsciiPanel;
import ru.mit.spbau.model.Items.*;
import ru.mit.spbau.model.game.Game;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.map.MapCell;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.strategies.users.DefaultPlayerStrategy;
import ru.mit.spbau.model.units.Attributes;
import ru.mit.spbau.model.units.Inventory;
import ru.mit.spbau.model.units.creeps.CreepUnit;
import ru.mit.spbau.model.units.users.PlayerUnit;
import ru.mit.spbau.model.units.users.UserMove;
import ru.mit.spbau.view.GUI;
import ru.mit.spbau.AppMain;

public final class PlayScreen implements Screen, GUI {

    @NotNull private static final Logger LOGGER = Logger.getLogger(StartScreen.class.getName());

    @NotNull private final DefaultPlayerStrategy playerStrategy;
    @NotNull private final Game game;

    private RelativeMap map;
    private int score;

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
            displayScore(terminal);
            displayAttributes(terminal);
            displayInventory(terminal);
        }
        terminal.writeCenter("-- press [escape] to exit game --", AppMain.getTerminalHeight() - 1);
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
        AppMain.getViewManager().setScreen(new LoseScreen(userName, score));
    }

    @Override
    public void notifyWin(@NotNull String userName, int score) {
        LOGGER.info("Notified win.");
        AppMain.getViewManager().setScreen(new WinScreen(userName, score));
    }

    @Override
    public void drawMap(@NotNull RelativeMap map, int score) {
        LOGGER.info("drawing new map");
        this.map = map;
        this.score = score;
        AppMain.getViewManager().setScreen(this);
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

    private void displayScore(@NotNull AsciiPanel terminal) {
        terminal.write("score: " + score, 1, AppMain.getTerminalHeight() - 4);
    }

    private void displayAttributes(@NotNull AsciiPanel terminal) {
        final PlayerUnit playerUnit = (PlayerUnit)map.getSelf();
        final Attributes attributes = playerUnit.getAttributes();

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("attributes:   hp: ");
        stringBuilder.append(attributes.getCurrentHp());
        stringBuilder.append("/");
        stringBuilder.append(attributes.getMaxHp());

        stringBuilder.append(";  attack: ");
        stringBuilder.append(attributes.getAttack());

        stringBuilder.append(";  vision range: ");
        stringBuilder.append(attributes.getVisionRange());

        terminal.write(stringBuilder.toString(), 1, AppMain.getTerminalHeight() - 3);
    }

    private void displayInventory(@NotNull AsciiPanel terminal) {
        final PlayerUnit playerUnit = (PlayerUnit)map.getSelf();
        final Inventory inventory = playerUnit.getInventory();

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("inventory:   ");
        inventory.getItems().forEach(i -> {
            stringBuilder.append(i.getName());
            stringBuilder.append("; ");
        });

        terminal.write(stringBuilder.toString(), 1, AppMain.getTerminalHeight() - 2);
    }

    private void displayGameObject(@NotNull GameObject object, @NotNull AsciiPanel terminal) {
        if (object instanceof CreepUnit) {
            terminal.write('Z', object.getPosition().getX(), object.getPosition().getY(), Color.RED);
        }
        if (object instanceof PlayerUnit) {
            terminal.write('@', object.getPosition().getX(), object.getPosition().getY(), Color.YELLOW);
        }
        if (object instanceof SimpleArmor) {
            terminal.write('A', object.getPosition().getX(), object.getPosition().getY(), Color.ORANGE);
        }
        if (object instanceof SimpleSword) {
            terminal.write('s', object.getPosition().getX(), object.getPosition().getY(), Color.CYAN);
        }
        if (object instanceof CoolSword) {
            terminal.write('S', object.getPosition().getX(), object.getPosition().getY(), Color.MAGENTA);
        }
        if (object instanceof HealingPotion) {
            terminal.write('+', object.getPosition().getX(), object.getPosition().getY(), Color.GREEN);
        }
        if (object instanceof Graal) {
            terminal.write('G', object.getPosition().getX(), object.getPosition().getY(), Color.YELLOW);
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
            case KeyEvent.VK_SPACE:
                return UserMove.PICK;
            default:
                return UserMove.UNKNOWN;
        }
    }

}
