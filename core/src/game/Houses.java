package game;

import util.opengl.Geometry;
import util.opengl.Material;
import util.opengl.Mesh;
import util.opengl.attributes.FloatAttribute;

import static org.lwjgl.opengl.GL11C.GL_POINTS;

/**
 * Blurred screen using framebuffer
 */
public class Houses extends ScreenController {
    public Houses() {
        // Entity creation
        float points[] = {
                -0.5f, 0.5f,
                0.5f, 0.5f,
                0.5f, -0.5f,
                -0.5f, -0.5f,
        };

        float[] colors = {
                1.0f, 0.0f, 0.0f, // top-left
                0.0f, 1.0f, 0.0f, // top-right
                0.0f, 0.0f, 1.0f, // bottom-righ
                1.0f, 1.0f, 0.0f  // bottom-leftn
        };

        Mesh pointMesh = new Mesh().setGeometry(
                new Geometry()
                        .addAttribute("aPos", new FloatAttribute(2, points, false))
                        .addAttribute("aColor", new FloatAttribute(3, colors, false))
        ).setMat(new Material().setShader("house"));
        engine.createEntity(pointMesh);

        engine.addSystem((engine, delta) -> {
            engine.findEntitiesWith(Mesh.class).forEach((result -> {
                Mesh mesh = result.components;
                mesh.begin();
                mesh.render(GL_POINTS);
                mesh.end();
            }));
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
