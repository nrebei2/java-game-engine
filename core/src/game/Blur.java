package game;

import game.components.Transform;
import util.Vector3;
import util.opengl.Mesh;
import util.opengl.MeshPrimitives;

import java.util.Random;

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
                       new Vector3(3, 0, 0),
                       new Vector3(),
                       new Vector3(1, 1, 1)
                )
        );

        // Very basic rendering system with camera
        engine.addSystem((engine, delta) ->
                engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                    var pair = result.components;
                    Mesh mesh = pair.comp1;
                    Transform transform = pair.comp2;
                    mesh.setCombinedMatrix(camera.getViewProj());
                    mesh.setModelMatrix(transform.getModel());
                    mesh.render();
                }))
        );
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
