package util;

/**
 * Registers a callback function for a Screen's exit request
 * <p>
 * Used for Game classes to communicate with their Screens
 */
public interface ScreenObserver {

    /**
     * The given screen has made a request to exit.
     * <p>
     * The value exitCode can be used to implement menu options.
     *
     * @param screen   The screen requesting to exit
     * @param exitCode The state of the screen upon exit
     */
    void exitScreen(Screen screen, int exitCode);
}
