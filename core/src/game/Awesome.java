package game;

import game.components.Transform;
import util.Vector3;
import util.opengl.Material;
import util.opengl.Mesh;
import util.opengl.MeshPrimitives;

import java.util.Random;

/**
 * Screen with 10000 instantiated cubes with a :D texture on it.
 */
public class Awesome extends ScreenController {
    private final Random gen;

    public Awesome() {
        this.gen = new Random();

        // Entity creation
        Mesh cube = MeshPrimitives.Cube().setMat(
                new Material("cube",
                        new Material.Texture("awesomeface.png", "texture1")
                )
        );
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

        // Very basic rendering system with camera
        engine.addSystem((engine, delta) -> {
            // Small optimization, we are only using one shader
            cube.begin();
            cube.setCombinedMatrix(camera.getViewProj());
            cube.end();
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.begin();
                //((FloatAttribute) mesh.getGeo().getAttribute("aPos")).data[0] = rand(-10f, 10f);
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
                mesh.end();
            }));
        });
    }

    private float rand(float min, float max) {
        return min + gen.nextFloat() * (max - min);
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
