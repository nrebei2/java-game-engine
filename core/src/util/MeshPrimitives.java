package util;

import util.attributes.FloatAttribute;

public class MeshPrimitives {
    /**
     * @return 1x1x1 cube centered at (0, 0, 0) with supplied uvs.
     */
    public static Mesh Cube() {
        float[] vertices = {
                -0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                // Front face
                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                // Left face
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                // Right face
                0.5f,  0.5f,  0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                // Bottom face
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,
                // Top face
                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f,  0.5f,
        };

        float[] uvs = {
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
        return cube;
    }
}
