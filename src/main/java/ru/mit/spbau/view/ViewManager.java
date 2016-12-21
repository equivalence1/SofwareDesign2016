package ru.mit.spbau.view;

import asciiPanel.AsciiPanel;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.Main;
import ru.mit.spbau.view.screens.Screen;
import ru.mit.spbau.view.screens.StartScreen;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * In this class we listen for users key presses and change
 * our screen on each press.
 *
 * As we have only 1 screen (I mean real computer screen) this
 * class is a singleton
 */
public final class ViewManager extends JFrame implements KeyListener {

    private static final int HEIGHT = 24;
    private static final int WIDTH = 80;

    private static ViewManager INSTANCE;

    @NotNull private final Runnable exitAction;
    private AsciiPanel terminal;
    private Screen screen;

    private ViewManager(@NotNull Runnable exitAction) {
        super();

        this.exitAction = exitAction;
        terminal = new AsciiPanel(WIDTH, HEIGHT);
        screen = new StartScreen();

        add(terminal);
        pack();
        addKeyListener(this);
        repaint();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Get instance of this class
     * @return instance of this class
     */
    public static ViewManager getViewManager() {
        if (INSTANCE == null) {
            INSTANCE = new ViewManager(Main::exitAction);
        }
        return INSTANCE;
    }

    @Override
    public void repaint(){
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    /**
     * Change screen
     * @param screen new screen
     */
    public void setScreen(Screen screen) {
        this.screen = screen;
        if (screen == null) {
            exit();
        }
        repaint();
    }

    /**
     * Height of our AsciiTerminal
     * @return height of terminal
     */
    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Width of our AsciiTerminal
     * @return width of terminal
     */
    public int getWidth() {
        return WIDTH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(KeyEvent e) {
        setScreen(screen.respondToUserInput(e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(KeyEvent e) { }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyTyped(KeyEvent e) { }

    private void exit() {
        exitAction.run();
    }

}
