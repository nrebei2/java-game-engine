package game;

import game.components.Transform;
import util.Matrix4;
import util.Vector3;
import util.opengl.Mesh;
import util.opengl.MeshPrimitives;
import util.opengl.Skybox;

import static org.lwjgl.opengl.GL43.*;

/**
 * Ocean map using skybox
 */
public class Ocean extends ScreenController {
    public Ocean() {

        Mesh cube = MeshPrimitives.Cube();
        engine.createEntity(
                cube,
                new Transform(
                        new Vector3(3, 0, 0),
                        new Vector3(),
                        new Vector3(1, 1, 1)
                )
        );

        Skybox skybox = new Skybox("ocean", "jpg");

        // Same simple render system
        engine.addSystem((engine, delta) -> {
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.setCombinedMatrix(camera.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
            }));

            glDepthFunc(GL_LEQUAL); // Since cubemap depth values are at 1
            // Needed rotations to work well with OpenGL sampled direction
            Matrix4 proj = camera.getViewProj();
            proj.rotate((float) - Math.PI / 2, 0, 0);
            proj.rotate(0, (float) - Math.PI / 2,  0);
            proj.removeTranslation(); // Keep cube centered on camera
            skybox.getCubeMap().setCombinedMatrix(proj);
            skybox.getCubeMap().render();
            glDepthFunc(GL_LESS);
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
