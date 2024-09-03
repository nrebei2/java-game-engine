package util.opengl;

import util.opengl.attributes.FloatAttribute;

public class MeshPrimitives {
    /**
     * @return 1x1x1 cube centered at (0, 0, 0) with supplied uvs. No material attached.
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

        Mesh cube = new Mesh();
        Geometry tri = new Geometry()
                .addAttribute("aPos",
                        new FloatAttribute(3, cubePos, true)
                )
                .addAttribute("aTexCoord",
                        new FloatAttribute(2, cubeUvs, false));
        cube.setGeometry(tri);
        return cube;
    }

    /**
     * @return 1x1 quad on z=0. Thus, can be used as a screen quad with NDC. No material attached.
     */
    public static Mesh Quad() {
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

        Mesh quad = new Mesh();
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
