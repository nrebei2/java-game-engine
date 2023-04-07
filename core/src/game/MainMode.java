package game;

import game.components.Camera;
import game.components.Transform;
import util.GameEngine;
import util.Mesh;
import util.MeshPrimitives;
import util.Vector3;
import util.ecs.Engine;

import java.util.Random;

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
    private final Random gen;

    public MainMode() {
        engine = new Engine();
        this.gen = new Random();

        // Entity creation
        Mesh cube = MeshPrimitives.Cube();
        for (int i = 0; i < 10000; i++) {
            // Instancing could be used here, but this works too
            engine.createEntity(
                    cube,
                    new Transform(
                            new Vector3(rand(-100f, 100f), rand(-100f, 100f), rand(-100f, 100f)),
                            new Vector3(),
                            new Vector3(rand(0.4f, 1.3f), rand(0.4f, 1.3f), rand(0.4f, 1.3f))
                    )
            );
        }

        camera = new Camera(
                new Vector3(),
                new Vector3(),
                0.001f, 1000, (float) Math.PI / 2,
                (float) GameEngine.input.getScreenWidth() / GameEngine.input.getScreenHeight()
        );

        // Basic rendering system with created camera
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

        // Camera control system
        CameraController controller = new CameraController(camera, 1.5f, 4, 0.12f);
        engine.addSystem(controller);
    }

    private float rand(float min, float max) {
        return min + gen.nextFloat() * (max - min);
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
