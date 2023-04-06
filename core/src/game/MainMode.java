package game;

import game.components.Camera;
import game.components.Mesh;
import game.components.Transform;
import util.Geometry;
import util.ShaderProgram;
import util.Vector3;
import util.attributes.FloatAttribute;
import util.ecs.Engine;

public class MainMode implements Screen {
    /**
     * ECS engine
     */
    Engine engine;

    public MainMode() {
        engine = new Engine();

        Mesh triangle = new Mesh(new ShaderProgram("cube"));
        Geometry tri = new Geometry()
                .addAttribute("aPos",
                        new FloatAttribute(3, new float[]{
                                0.5f, 0.5f, 0.0f,  // top right
                                0.5f, -0.5f, 0.0f,  // bottom right
                                -0.5f, -0.5f, 0.0f,  // bottom left
                                -0.5f, 0.5f, 0.0f   // top left
                        }, false))
                .setIndices(new int[]{
                                0, 1, 3,   // first triangle
                                1, 2, 3    // second triangle
                        }
                );
        triangle.setGeometry(tri);

        // Entity creation
        engine.createEntity(triangle, new Transform(new Vector3(3, 0, 0), new Vector3(), new Vector3(1, 1, 1)));
        engine.createEntity(new Camera(new Vector3(), new Vector3(), 0.01f, 100, (float) Math.PI, 8 / 6));

        // Basic rendering system with camera
        engine.addSystem((engine) -> {
            var camera = engine.findEntitiesWith(Camera.class).iterator().next().components;
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.setCombinedMatrix(camera.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
            }));
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        engine.run();
    }

    @Override
    public void resize(int width, int height) {

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
