package game;

import game.components.Transform;
import util.Vector3;
import util.opengl.*;

/**
 * Blurred screen using framebuffer
 */
public class Blur extends ScreenController {

    /**
     * Screen-space quad
     */
    private Mesh quad;
    private FrameBuffer buffer;

    public Blur() {
        // Entity creation
        Mesh cube = MeshPrimitives.Cube().setMat(
                new Material("cube")
                        .addTexture(new TextureManager.Texture("awesomeface.png"), "texture1")
        );
        engine.createEntity(
                cube,
                new Transform(
                        new Vector3(3, 0, 0),
                        new Vector3(),
                        new Vector3(1, 1, 1)
                )
        );

        this.quad = MeshPrimitives.Quad().setMat(new Material("screenspace"));

        // Multi-pass render system
        engine.addSystem((engine, delta) -> {
            // First pass
            buffer.bind();
            engine.findEntitiesWith(Mesh.class, Transform.class).forEach((result -> {
                var pair = result.components;
                Mesh mesh = pair.comp1;
                Transform transform = pair.comp2;
                mesh.begin();
                mesh.setCombinedMatrix(camera.getViewProj());
                mesh.setModelMatrix(transform.getModel());
                mesh.render();
                mesh.end();
            }));
            buffer.unbind();

            // Next pass
            quad.begin();
            quad.render();
            quad.end();
        });
    }

    @Override
    public void show() {
        super.show();
        createFrameBuffer();
    }

    private void createFrameBuffer() {
        buffer = new FrameBuffer();
        quad.getMat().removeTexture("color");
        quad.getMat().addFBOColorTex(buffer, "color");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        createFrameBuffer();
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
