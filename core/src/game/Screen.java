package game;

public interface Screen {
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    public void show();

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta);

    public void resize(int width, int height);

    public void pause();

    public void resume();

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    public void hide();

    /**
     * Called when this screen should release all resources.
     */
    public void dispose();
}
