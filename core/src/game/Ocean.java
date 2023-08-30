package game;

import game.components.Transform;
import util.Matrix4;
import util.Vector3;
import util.opengl.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL43.*;

/**
 * Ocean map with tree using skybox
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
        Mesh ground = MeshPrimitives.Quad().setMat(
                new Material()
                        .setShader("cube")
                        .addTexture(new TextureManager.Texture("grass.png"), "texture1")
        );
        engine.createEntity(
                ground,
                new Transform(
                        new Vector3(3, 0, -1),
                        new Vector3(),
                        new Vector3(2, 2, 1)
                )
        );

        Model tree = new Model("assets/models/tree/tree1low.obj", "assets/models/tree");
        tree.setShader("tree");
        engine.createEntity(
                tree,
                new Transform(
                        new Vector3(3, 0, -1),
                        new Vector3((float) Math.PI / 2, 0, 0),
                        new Vector3(0.001f, 0.001f, 0.001f)
                )
        );

        float length = 8192 * 3;
        Mesh clouds = MeshPrimitives.Quad().setMat(new Material().setShader("clouds"));
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

        // Hardcoded constants
        Vector3 sunDir = new Vector3(-0.45399049974f, -0.89100652419f, 0.43837114679f);
        tree.begin();
        tree.setVec3f("u_sunDir", sunDir);
        Vector3 sunColor = new Vector3(1.8f, 2.0f, 2.5f);
        tree.setVec3f("u_sunColor", sunColor);
        tree.end();

        // Simple render system with transparency support
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
                        mesh.render();
                        mesh.end();
                    }));

            engine.findEntitiesWith(Model.class, Transform.class)
                    .filter((result) -> !result.entity.containsType(Transparent.class))
                    .forEach((result -> {
                        var pair = result.components;
                        Model model = pair.comp1;
                        Transform transform = pair.comp2;
                        model.begin();
                        model.setVec3f("u_viewPos", camera.getTransform().getPosition());
                        model.setCombinedMatrix(camera.getViewProj());
                        model.setFloat("u_time", (float) glfwGetTime());
                        model.setModelMatrix(transform.getModel());
                        model.render();
                        model.end();
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
