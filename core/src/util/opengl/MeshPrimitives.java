package util.opengl;

import util.opengl.attributes.FloatAttribute;

public class MeshPrimitives {
    /**
     * @return 1x1x1 cube centered at (0, 0, 0) with supplied uvs. Used shader is cube.
     */
    public static Mesh Cube() {
        float[] cubePos = {
                // Back face
                -0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                // Front face
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                // Left face
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                // Right face
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                // Bottom face
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                // Top face
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
        };

        float[] cubeUvs = {
                0.0f, 0.0f, // Bottom-left
                1.0f, 1.0f, // top-right
                1.0f, 0.0f, // bottom-right
                1.0f, 1.0f, // top-right
                0.0f, 0.0f, // bottom-left
                0.0f, 1.0f, // top-left

                0.0f, 0.0f, // bottom-left
                1.0f, 0.0f, // bottom-right
                1.0f, 1.0f, // top-right
                1.0f, 1.0f, // top-right
                0.0f, 1.0f, // top-left
                0.0f, 0.0f, // bottom-left

                1.0f, 0.0f, // top-right
                1.0f, 1.0f, // top-left
                0.0f, 1.0f, // bottom-left
                0.0f, 1.0f, // bottom-left
                0.0f, 0.0f, // bottom-right
                1.0f, 0.0f, // top-right

                1.0f, 0.0f, // top-left
                0.0f, 1.0f, // bottom-right
                1.0f, 1.0f, // top-right
                0.0f, 1.0f, // bottom-right
                1.0f, 0.0f, // top-left
                0.0f, 0.0f, // bottom-left

                0.0f, 1.0f, // top-right
                1.0f, 1.0f, // top-left
                1.0f, 0.0f, // bottom-left
                1.0f, 0.0f, // bottom-left
                0.0f, 0.0f, // bottom-right
                0.0f, 1.0f, // top-right

                0.0f, 1.0f, // top-left
                1.0f, 0.0f, // bottom-right
                1.0f, 1.0f, // top-right
                1.0f, 0.0f, // bottom-right
                0.0f, 1.0f, // top-left
                0.0f, 0.0f  // bottom-left
        };

        Mesh cube = new Mesh(
                new Material("cube",
                        new Material.Texture("awesomeface.png", "texture1")
                )
        );
        Geometry tri = new Geometry()
                .addAttribute("aPos",
                        new FloatAttribute(3, cubePos, false)
                )
                .addAttribute("aTexCoord",
                        new FloatAttribute(2, cubeUvs, false));
        cube.setGeometry(tri);
        return cube;
    }

    /**
     * @return quad that fills the entire screen in NDC w/ uvs. Used shader is screenspace.
     */
    public static Mesh ScreenQuad() {
        float[] quadPos = {
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, -1.0f,
                -1.0f, 1.0f,
                1.0f, -1.0f,
                1.0f, 1.0f,
        };

        float[] quadUv = {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        Mesh quad = new Mesh(
                new Material("screenspace")
        );
        Geometry geo = new Geometry()
                .addAttribute("aPos",
                        new FloatAttribute(2, quadPos, false)
                )
                .addAttribute("aTexCoord",
                        new FloatAttribute(2, quadUv, false));
        quad.setGeometry(geo);
        return quad;
    }
}
