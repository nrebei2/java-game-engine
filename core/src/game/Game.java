package game;

public abstract class Game {
    protected Screen curScreen;

    public abstract void create();

    /**
     * Called when the game should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta) {
        if (curScreen != null) curScreen.render(delta);
    }

    public void resize(int width, int height) {
        if (curScreen != null) curScreen.resize(width, height);
    }

    public void pause() {
        if (curScreen != null) curScreen.pause();
    }

    public void resume() {
        if (curScreen != null) curScreen.resume();
    }

    /**
     * Called when this game should release all resources.
     */
    public abstract void dispose();

    public void setScreen(Screen screen) {
        if (this.curScreen != null) curScreen.hide();
        if (screen != null) {
            curScreen = screen;
            screen.resume();
        }
    }

    public Screen getScreen() {
        return curScreen;
    }

}
