package ru.mit.spbau;

import ru.mit.spbau.view.ViewManager;

/**
 * Just our main which goes directly to main menu.
 */
public final class Main {

    public static void main(String[] args) {
        ViewManager viewManager = ViewManager.getViewManager();
    }

    public static void exitAction() {
        System.exit(0);
    }

}
