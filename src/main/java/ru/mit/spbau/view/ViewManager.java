package ru.mit.spbau.view;

import ru.mit.spbau.view.screens.Screen;
import ru.mit.spbau.view.screens.StartScreen;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Our main view class.
 * Responds to user's presses and controls screens
 */
public class ViewManager extends JFrame implements KeyListener {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 28;

    private final AsciiPanel terminal;
    private Screen screen;

    private static ViewManager INSTANCE;

    public ViewManager(){
        super();
        terminal = new AsciiPanel(WIDTH, HEIGHT);
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
        INSTANCE = this;
    }

    public void repaint(){
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    public static int getTerminalWidth() {
        return WIDTH;
    }

    public static int getTerminalHeight() {
        return HEIGHT;
    }

    /**
     * Get instance of this class
     * @return instance of this class
     */
    public static ViewManager getViewManager() {
        if (INSTANCE == null) {
            return new ViewManager();
        }
        return INSTANCE;
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
        System.exit(0);
    }

    public static void main(String[] args) {
        ViewManager app = new ViewManager();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
