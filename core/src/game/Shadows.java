package game;

import game.components.Transform;
import util.Matrix4;
import util.Vector3;
import util.opengl.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL43.*;

/**
 * Directional light shadow map
 */
public class Shadows extends ScreenController {

    /**
     * Elapsed time in seconds
     */
    private float elapsed = 0;

    public Shadows() {
        // Entity creation
        Mesh ground = MeshPrimitives.Quad().setMat(
                new Material()
                        .setShader("cube")
                        .addTexture(new TextureManager.Texture("wood.png"), "texture1")
        );


        engine.addSystem((engine, delta) -> {

        });
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
