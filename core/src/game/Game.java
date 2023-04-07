package game;

public abstract class Game {
    protected Screen curScreen;

    /**
     * Called right after the application has initialized.
     */
    public abstract void create();

    /**
     * Called when the game should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta) {
        if (curScreen != null) curScreen.render(delta);
    }

    /**
     * Called when the window resizes
     */
    public void resize(int width, int height) {
        if (curScreen != null) curScreen.resize(width, height);
    }

    /**
     * Called when the window is minimized
     */
    public void pause() {
        if (curScreen != null) curScreen.pause();
    }

    /**
     * Called when the window is restored
     */
    public void resume() {
        if (curScreen != null) curScreen.resume();
    }

    /**
     * Called when this game should release all resources.
     */
    public abstract void dispose();

    /**
     * Sets the current screen for this Game
     */
    public void setScreen(Screen screen) {
        if (this.curScreen != null) curScreen.hide();
        if (screen != null) {
            curScreen = screen;
            screen.resume();
        }
    }
}
