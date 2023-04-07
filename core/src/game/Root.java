package game;

import util.Game;
import util.GameEngine;
import util.Screen;
import util.ScreenObserver;

/**
 * Root game for this application
 */
public class Root extends Game implements ScreenObserver {

    ScreenController[] screens = new ScreenController[2];
    int curScreen;

    @Override
    public void create() {
        screens[0] = new Awesome();
        screens[1] = new Awesome();

        screens[0].setObserver(this);
        screens[1].setObserver(this);

        curScreen = 0;
        setScreen(screens[curScreen]);
    }

    /**
     * Called when this game should release all resources.
     */
    @Override
    public void dispose() {

    }

    @Override
    public void exitScreen(Screen screen, int exitCode) {
        if (exitCode == ScreenController.CODE_NEXT) {
            curScreen = (curScreen + 1) % screens.length;
            setScreen(screens[curScreen]);
        } else if (exitCode == ScreenController.CODE_BACK) {
            curScreen = (curScreen + screens.length - 1) % screens.length;
            setScreen(screens[curScreen]);
        }
    }
}
