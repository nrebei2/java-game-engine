package game;

import util.Matrix4;
import util.opengl.Mesh;
import util.opengl.Model;
import util.opengl.Skybox;
import util.opengl.attributes.FloatAttribute;

import java.util.Arrays;
import java.util.Random;

import static org.lwjgl.opengl.GL43.*;

/**
 * Ocean map with tree using skybox
 */
public class Space extends ScreenController {

    private final Random gen = new Random();

    public Space() {
        Model rock = new Model("assets/models/rock/rock.obj", "assets/models/rock");
        rock.setShader("rock");
        Skybox skybox = new Skybox("space", "png");

        float radius = 75f;
        int count = 5000;
        float offset = 25f;
        Matrix4[] mats = new Matrix4[count];
        for (int i = 0; i < count; i++) {
            Matrix4 m = new Matrix4();
            m.translate(rand(-offset, offset), rand(-offset, offset), rand(-offset, offset));
            m.translate(radius * (float) Math.cos(2 * Math.PI * i / count), radius * 1.35f * (float) Math.sin(2 * Math.PI * i / count), 0);
            m.rotate(rand(0, (float) Math.PI * 2), rand(0, (float) Math.PI * 2), rand(0, (float) Math.PI * 2));
            m.scale(rand(0.75f, 1.25f), rand(0.75f, 1.25f), rand(0.75f, 1.25f));
            mats[i] = m;
        }

        FloatAttribute instanceData = new FloatAttribute(mats, false, true);
        for (Mesh mesh : rock.getMeshes()) {
            mesh.getGeo().addAttribute("aInstanceMatrix", instanceData);
        }

        engine.addSystem((engine, delta) -> {
            rock.begin();
            rock.setCombinedMatrix(camera.getViewProj());
            rock.renderInstanced(count);
            rock.end();

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
        });
    }

    private float rand(float min, float max) {
        return min + gen.nextFloat() * (max - min);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
