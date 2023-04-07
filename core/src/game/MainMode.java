package game;

import game.components.Camera;
import game.components.Mesh;
import game.components.Transform;
import util.GameEngine;
import util.Geometry;
import util.ShaderProgram;
import util.Vector3;
import util.attributes.FloatAttribute;
import util.ecs.Engine;

import static org.lwjgl.opengl.GL11.glViewport;

public class MainMode implements Screen {
    /**
     * ECS engine
     */
    Engine engine;

    /**
     * Camera for this scene
     */
    Camera camera;

    public MainMode() {
        engine = new Engine();

        // 1x1x1 cube
        float[] vertices = {
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,

                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
        };

        Mesh cube = new Mesh(new ShaderProgram("cube"));
        Geometry tri = new Geometry()
                .addAttribute("aPos",
                        new FloatAttribute(3, vertices, false));
        cube.setGeometry(tri);

        // Entity creation
        engine.createEntity(
                cube,
                new Transform(
                        new Vector3(3, 0, 0),
                        new Vector3(),
                        new Vector3(1, 1, 1)
                )
        );

        camera = new Camera(
                new Vector3(),
                new Vector3(),
                0.001f, 100, (float) Math.PI / 2,
                (float) GameEngine.input.getScreenWidth() / GameEngine.input.getScreenHeight()
        );
        engine.createEntity(camera);

        // Basic rendering system with camera
        engine.addSystem((engine, delta) -> {
            var cam = engine.findEntitiesWith(Camera.class).iterator().next().components;
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.setCombinedMatrix(cam.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
            }));
        });

        // Camera control system
        CameraController controller = new CameraController(camera, 1, 2, 0.03f);
        engine.addSystem(controller);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        engine.run(delta);
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        camera.setAsp((float) width / height);
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
