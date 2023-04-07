package util;

public interface Screen {
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    public void show();

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    public void hide();

    /**
     * Called when this screen should release all resources.
     */
    public void dispose();

    /**
     * See {@link Game#render(float)}
    */
    public void render(float delta);
    /**
     * See {@link Game#resize(int, int)}
     */
    public void resize(int width, int height);
    /**
     * See {@link Game#pause()}
     */
    public void pause();
    /**
     * See {@link Game#resume()}
     */
    public void resume();
}
