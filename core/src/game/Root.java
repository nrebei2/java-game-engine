package game;

import util.Game;
import util.Screen;
import util.ScreenObserver;

/**
 * Root game for this application
 */
public class Root extends Game implements ScreenObserver {

    ScreenController[] screens = new ScreenController[3];
    int curScreen;

    @Override
    public void create() {
        screens[0] = new Awesome();
        screens[1] = new Blur();
        screens[2] = new Ocean();

        screens[0].setObserver(this);
        screens[1].setObserver(this);
        screens[2].setObserver(this);

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
            screens[curScreen].nextPrevious = true;
            setScreen(screens[curScreen]);
        } else if (exitCode == ScreenController.CODE_BACK) {
            curScreen = (curScreen + screens.length - 1) % screens.length;
            screens[curScreen].prevPrevious = true;
            setScreen(screens[curScreen]);
        }
    }
}
