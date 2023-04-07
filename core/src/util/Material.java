package util;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.StructBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Material {
    /**
     * Map of textures to their OpenGL id
     */
    public Map<Texture, Integer> texs;

    /** Shader of this material */
    public ShaderProgram shader;

    /**
     *
     * @param shaderName See {@link ShaderProgram}
     * @param textures Textures this material will maintain
     */
    public Material(String shaderName, Texture... textures) {
        this.shader = new ShaderProgram(shaderName);
        this.texs = new HashMap<>();

        addTextures(textures);
    }

    /**
     * Add a texture to this material. It is safe to add a texture anytime.
     * @param textures Textures this material will maintain
     */
    public void addTextures(Texture... textures) {
        int[] ids = new int[textures.length];
        glGenTextures(ids);
        for (int i = 0; i < textures.length; i++) {
            loadTex(ids[i], textures[i]);
            texs.put(textures[i], ids[i]);
        }
    }

    /**
     * Helper function to load a texture through stb_image
     */
    private void loadTex(int id, Texture texture)
    {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, texture.sWrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, texture.tWrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texture.min);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texture.mag);
        // load image, create texture and generate mipmaps
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1); // int*
            IntBuffer height = stack.mallocInt(1); // int*
            IntBuffer nrChannels = stack.mallocInt(1); // int*
            String path = "assets/textures/" + texture.name;
            ByteBuffer data = stbi_load(path, width, height, nrChannels, 4);
            if (data != null) {
                int w = width.get(0);
                int h = height.get(0);
                int levels = (int)Math.min(log2(w), log2(h));
                glTexStorage2D(GL_TEXTURE_2D, levels, texture.internalFormat, w, h);
                glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, texture.abstractFormat, texture.type, data);
                glGenerateMipmap(GL_TEXTURE_2D);
                stbi_image_free(data);
            }
            else {
                System.err.printf("Failed to load texture %s\n", path);
                glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, 0, 0);
            }
        }
    }

    private double log2(int w) {
       return (Math.log(w) / Math.log(2));
    }

    public static class Texture {
       public String name;
       public String uniformName;
       public int min = GL_LINEAR_MIPMAP_LINEAR;
       public int mag = GL_LINEAR;
       public int sWrap = GL_REPEAT;
       public int tWrap = GL_REPEAT;
       public int internalFormat = GL_RGBA8;
       public int abstractFormat = GL_RGBA;
       public int type = GL_UNSIGNED_BYTE;

       /**
        * @param name Name of file with type (.png, .jpg, etc.). Assumed to be located in assets/textures.
        * @param uniformName Name of uniform in shader that will hold this texture.
        */
       public Texture(String name, String uniformName) {
           this.name = name;
           this.uniformName = uniformName;
       }
   }
}
