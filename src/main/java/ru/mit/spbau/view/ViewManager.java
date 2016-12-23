package ru.mit.spbau.view;

import ru.mit.spbau.view.screens.Screen;
import ru.mit.spbau.view.screens.StartScreen;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ViewManager extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;

    private static ViewManager INSTANCE;

    public ViewManager(){
        super();
        terminal = new AsciiPanel();
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

    public void keyPressed(KeyEvent e) {
        setScreen(screen.respondToUserInput(e));
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

    public void keyReleased(KeyEvent e) { }

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
