package game;

import game.Game;
import game.MainMode;

/**
 * Root game for this application
 */
public class Root extends Game {

    @Override
    public void create() {
        setScreen(new MainMode());
    }

    @Override
    public void dispose() {

    }
}
