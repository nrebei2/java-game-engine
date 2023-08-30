package util;

public interface Screen {
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    void show();

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    void hide();

    /**
     * Called when this screen should release all resources.
     */
    void dispose();

    /**
     * See {@link Game#render(float)}
     */
    void render(float delta);

    /**
     * See {@link Game#resize(int, int)}
     */
    void resize(int width, int height);

    /**
     * See {@link Game#pause()}
     */
    void pause();

    /**
     * See {@link Game#resume()}
     */
    void resume();
}
