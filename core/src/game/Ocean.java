package game;

import game.components.Transform;
import util.Matrix4;
import util.Vector3;
import util.opengl.Material;
import util.opengl.Mesh;
import util.opengl.MeshPrimitives;
import util.opengl.Skybox;

import static org.lwjgl.opengl.GL43.*;

/**
 * Ocean map using skybox
 */
public class Ocean extends ScreenController {

    /**
     * Elapsed time in seconds
     */
    private float elapsed = 0;

    /**
     * Transparency tag
     */
    private class Transparent {
    }

    public Ocean() {
        // Entity creation
        Mesh cube = MeshPrimitives.Cube().setMat(
                new Material("cube",
                        new Material.Texture("awesomeface.png", "texture1")
                )
        );
        engine.createEntity(
                cube,
                new Transform(
                        new Vector3(3, 0, 0),
                        new Vector3(),
                        new Vector3(1, 1, 1)
                )
        );


        float length = 8192 * 3;
        Mesh clouds = MeshPrimitives.Quad().setMat(new Material("clouds"));
        engine.createEntity(
                clouds,
                new Transform(
                        new Vector3(0, 0, 3000f),
                        new Vector3(),
                        new Vector3(length, length, 1)
                ),
                new Transparent()
        );

        Skybox skybox = new Skybox("ocean", "jpg");

        // Same simple render system
        engine.addSystem((engine, delta) -> {

            // First render opaque objects
            engine.findEntitiesWith(Mesh.class, Transform.class)
                    .filter((result) -> !result.entity.containsType(Transparent.class))
                    .forEach((result -> {
                        var pair = result.components;
                        Mesh mesh = pair.comp1;
                        Transform transform = pair.comp2;
                        mesh.begin();
                        mesh.setCombinedMatrix(camera.getViewProj());
                        mesh.setModelMatrix(transform.getModel());
                        mesh.setModelMatrix(transform.getModel());
                        mesh.getMat().setFloat("iTime", elapsed);
                        mesh.render();
                        mesh.end();
                    }));

            // Render skybox
            glDepthFunc(GL_LEQUAL); // Since cubemap depth values are at 1
            // Needed rotations to work well with OpenGL sampled direction
            Matrix4 proj = camera.getViewProj();
            proj.rotate((float) -Math.PI / 2, 0, 0);
            proj.rotate(0, (float) -Math.PI / 2, 0);
            proj.removeTranslation(); // Keep cube centered on camera
            skybox.getCubeMap().begin();
            skybox.getCubeMap().setCombinedMatrix(proj);
            skybox.getCubeMap().render();
            skybox.getCubeMap().end();
            glDepthFunc(GL_LESS);

            // Finally, render transparent objects
            engine.findEntitiesWith(Mesh.class, Transform.class)
                    .filter((result) -> result.entity.containsType(Transparent.class))
                    .forEach((result -> {
                        var pair = result.components;
                        Mesh mesh = pair.comp1;
                        Transform transform = pair.comp2;
                        mesh.begin();
                        mesh.setCombinedMatrix(camera.getViewProj());
                        mesh.setModelMatrix(transform.getModel());
                        mesh.setModelMatrix(transform.getModel());
                        mesh.getMat().setFloat("iTime", elapsed);
                        mesh.render();
                        mesh.end();
                    }));
        });
    }

    @Override
    public void show() {
        super.show();
        elapsed = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        elapsed += delta;
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
