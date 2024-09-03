package util;

/**
 * Basic configuration class for application initialization
 */
public class Config {
    /**
     * Initial width (in pixels) of application. If fullscreen, this will be the first size when exiting full-screen mode
     */
    public int width = 800;
    /**
     * Initial height (in pixels) of application. If fullscreen, this will be the first size when exiting full-screen mode
     */
    public int height = 600;
    /**
     * Whether the window is resizable
     */
    public boolean resizable = true;

    /**
     * Title of window
     */
    public String title = "Hello World!";
    /**
     * Begin the application fullscreen
     */
    public boolean fullScreen = false;
}
