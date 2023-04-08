package game;

import game.components.Transform;
import org.lwjgl.glfw.GLFW;
import util.GameEngine;
import util.Vector3;
import util.opengl.FrameBuffer;
import util.opengl.Mesh;
import util.opengl.MeshPrimitives;

import java.util.Random;

import static org.lwjgl.opengl.GL43.*;

/**
 * Blurred screen using framebuffer
 */
public class Blur extends ScreenController {
    public Blur() {
        // Entity creation
        Mesh cube = MeshPrimitives.Cube();
        engine.createEntity(
                cube,
                new Transform(
                       new Vector3(3, -1, 0),
                       new Vector3(),
                       new Vector3(1, 1, 1)
                )
        );

        FrameBuffer buffer = new FrameBuffer();
        Mesh quad = MeshPrimitives.ScreenQuad();
        quad.getMat().addFBOColorTex(buffer, "color");

        // Render system
        engine.addSystem((engine, delta) -> {
            // First pass
            buffer.bind();
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                if (GameEngine.input.isKeyPressed(GLFW.GLFW_KEY_ENTER)) return;
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                transform.setScale(transform.getScale().scl(1.001f));
                mesh.setCombinedMatrix(camera.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
            }));
            buffer.unbind();

            // Next pass
            glDisable(GL_DEPTH_TEST);
            //cube.render();
            quad.render();
            glEnable(GL_DEPTH_TEST);
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
