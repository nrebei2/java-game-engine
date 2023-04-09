package util.opengl;

import org.lwjgl.system.MemoryStack;
import util.opengl.attributes.FloatAttribute;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Represents a cube map skybox
 */
public class Skybox {

    /**
     * Cube map positions
     */
    private static final float[] VERTS = {
            // bottom
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // back
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            // front
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            // top
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            // left
            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,

            // right
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
    };
    /**
     * Cubemap texture id
     */
    int texture;

    /**
     * Cube mesh holding skybox
     */
    Mesh cubeMap;

    /**
     * Creates a skybox mesh with a cubemap texture.
     * Attempts to locate textures name-[front, back, left, right, top, bottom].type in assets/textures/cubemaps
     */
    public Skybox(String name, String type) {
        this.texture = glGenTextures();
        loadMap(texture, name, type);

        // Add the cubemap texture manually
        this.cubeMap = new Mesh().setMat(new Material("cubemap"));
        cubeMap.getMat().texs.add(new Material.TexInfo(texture, "skybox"));

        cubeMap.setGeometry(new Geometry().addAttribute("aPos", new FloatAttribute(3, VERTS, false)));
    }

    public Mesh getCubeMap() {
        return cubeMap;
    }

    /**
     * Helper function to load a cube map texture through stb_image
     */
    private void loadMap(int id, String name, String type) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        // +-X, +-Y, +-Z
        String[] faces = {
                "right", "left", "bottom", "top", "front", "back"
        };

        stbi_set_flip_vertically_on_load(true);

        // load images
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1); // int*
            IntBuffer height = stack.mallocInt(1); // int*
            IntBuffer nrChannels = stack.mallocInt(1); // int*
            for (int i = 0; i < faces.length; i++) {
                String path = "assets/textures/cubemaps/" + name + "-" + faces[i] + "." + type;
                ByteBuffer data = stbi_load(path, width, height, nrChannels, 0);
                if (data != null) {
                    int w = width.get(0);
                    int h = height.get(0);
                    glTexImage2D(
                            GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB,
                            w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, data
                    );
                    stbi_image_free(data);
                } else {
                    System.err.printf("Failed to load texture %s\n", path);
                }
            }
        }
        stbi_set_flip_vertically_on_load(false);
    }
}
