package game;

import org.lwjgl.glfw.GLFW;
import util.GameEngine;
import util.Screen;
import util.ScreenObservable;

import static org.lwjgl.opengl.GL11.glViewport;

/** Allows the game to cycle between screens */
public abstract class ScreenController extends ScreenObservable implements Screen {

    public final static int CODE_NEXT = 0;
    public final static int CODE_BACK = 1;

    /** Used to force the exitScreen call only once when pressing key */
    private boolean nextPrevious = true;
    private boolean prevPrevious = true;

    @Override
    public void render(float delta) {
        // Switch screens on arrow key press
        boolean rightPressed = GameEngine.input.isKeyPressed(GLFW.GLFW_KEY_RIGHT);
        if (rightPressed && !nextPrevious) {
            observer.exitScreen(this, CODE_NEXT);
        }
        nextPrevious = rightPressed;

        boolean leftPressed = GameEngine.input.isKeyPressed(GLFW.GLFW_KEY_LEFT);
        if (leftPressed && !prevPrevious) {
            observer.exitScreen(this, CODE_BACK);
        }
        prevPrevious = leftPressed;

    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }
}
