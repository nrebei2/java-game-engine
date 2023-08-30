package game;

import util.Game;
import util.Screen;
import util.ScreenObservable;
import util.ScreenObserver;

import java.util.ArrayList;

/**
 * Root game for this application
 */
public class Root extends Game implements ScreenObserver {

    ArrayList<ScreenController> screens = new ArrayList<>();
    int curScreen;

    @Override
    public void create() {
        screens.add(new Awesome());
        screens.add(new Blur());
        screens.add(new Ocean());
        screens.add(new Space());
        screens.add(new Houses());

        for (ScreenObservable screen : screens) {
            screen.setObserver(this);
        }

        curScreen = 0;
        setScreen(screens.get(curScreen));
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
            curScreen = (curScreen + 1) % screens.size();
            setScreen(screens.get(curScreen));
        } else if (exitCode == ScreenController.CODE_BACK) {
            curScreen = (curScreen + screens.size() - 1) % screens.size();
            setScreen(screens.get(curScreen));
        }
    }
}
