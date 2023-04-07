package game;

import game.components.Camera;
import game.components.Mesh;
import game.components.Transform;
import util.*;
import util.attributes.FloatAttribute;
import util.ecs.Engine;

import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.opengl.GL11.GL_DONT_CARE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

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

        float[] uvs = {
                  0.0f, 0.0f,
                 1.0f, 0.0f,
                 1.0f, 1.0f,
                 1.0f, 1.0f,
                  0.0f, 1.0f,
                  0.0f, 0.0f,

                  0.0f, 0.0f,
                 1.0f, 0.0f,
                 1.0f, 1.0f,
                 1.0f, 1.0f,
                  0.0f, 1.0f,
                  0.0f, 0.0f,

                  1.0f, 0.0f,
                  1.0f, 1.0f,
                  0.0f, 1.0f,
                  0.0f, 1.0f,
                  0.0f, 0.0f,
                  1.0f, 0.0f,

                 1.0f, 0.0f,
                 1.0f, 1.0f,
                 0.0f, 1.0f,
                 0.0f, 1.0f,
                 0.0f, 0.0f,
                 1.0f, 0.0f,

                  0.0f, 1.0f,
                 1.0f, 1.0f,
                 1.0f, 0.0f,
                 1.0f, 0.0f,
                  0.0f, 0.0f,
                  0.0f, 1.0f,

                  0.0f, 1.0f,
                 1.0f, 1.0f,
                 1.0f, 0.0f,
                 1.0f, 0.0f,
                  0.0f, 0.0f,
                  0.0f, 1.0f
        };

        Mesh cube = new Mesh(
                new Material("cube",
                        new Material.Texture("awesomeface.png", "texture")
                )
        );
        Geometry tri = new Geometry()
                .addAttribute("aPos",
                        new FloatAttribute(3, vertices, false)
                )
                .addAttribute("aTexCoord",
                        new FloatAttribute(2, uvs, false));
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
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.setCombinedMatrix(camera.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
            }));
        });

        // Camera control system
        CameraController controller = new CameraController(camera, 1, 2, 0.1f);
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
