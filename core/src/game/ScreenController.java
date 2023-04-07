package game;

import game.components.Camera;
import org.lwjgl.glfw.GLFW;
import util.GameEngine;
import util.Screen;
import util.ScreenObservable;
import util.Vector3;
import util.ecs.Engine;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Simple scene with a controllable camera.
 * Also allows the game to cycle between screens
 */
public abstract class ScreenController extends ScreenObservable implements Screen {

    public final static int CODE_NEXT = 0;
    public final static int CODE_BACK = 1;

    /** Used to force the exitScreen call only once when pressing key */
    private boolean nextPrevious = true;
    private boolean prevPrevious = true;

    /**
     * ECS engine
     */
    Engine engine;

    /**
     * Camera for this scene
     */
    Camera camera;

    public ScreenController() {
        engine = new Engine();

        camera = new Camera(
                new Vector3(),
                new Vector3(),
                0.01f, 1000, (float) Math.PI / 2,
                (float) GameEngine.input.getScreenWidth() / GameEngine.input.getScreenHeight()
        );

        // Camera control system
        CameraController controller = new CameraController(camera, 1.5f, 4, 0.12f);
        engine.addSystem(controller);
    }

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

        engine.run(delta);
    }

    @Override
    public void show() {
        // Reset camera transform
        camera.getTransform().setRotation(new Vector3());
        camera.getTransform().setPosition(new Vector3());
    }

    @Override
    public void resize(int width, int height) {
        // Critical
        glViewport(0, 0, width, height);
        camera.setAsp((float) width / height);
    }
}
