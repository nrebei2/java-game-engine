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

        Mesh cube = MeshPrimitives.Cube();
        // Instancing could be used here, but this works too
        engine.createEntity(
                cube,
                new Transform(
                        new Vector3(3, 0, 0),
                        new Vector3(),
                        new Vector3(1, 1, 1)
                )
        );

        FrameBuffer buffer = new FrameBuffer();
        Mesh quad = MeshPrimitives.ScreenQuad();
        quad.getMat().addFBOColorTex(buffer, "color");

        // Multi-pass render system
        engine.addSystem((engine, delta) -> {
            // First pass
            buffer.bind();
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.setCombinedMatrix(camera.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
            }));
            buffer.unbind();

            // Next pass
            quad.render();
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
