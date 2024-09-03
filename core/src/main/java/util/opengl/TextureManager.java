package util.opengl;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;


/**
 * Manages (caches) textures.
 */
public class TextureManager {

    /**
     * Map of initialized textures to their OpenGL id
     */
    Map<Texture, Integer> textureMap;

    public TextureManager() {
        textureMap = new HashMap<>();
    }

    /**
     * Retrieves or generates an OpenGL texture.
     *
     * @param texture Texture id to retrieve
     * @return OpenGL id
     */
    public int getTexture(Texture texture) {
        if (textureMap.containsKey(texture)) return textureMap.get(texture);
        int id = glGenTextures();
        loadTex(id, texture);
        return id;
    }

    /**
     * Helper function to load a texture through stb_image
     */
    private void loadTex(int id, Texture texture) {
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
            String path = texture.name;
            ByteBuffer data = stbi_load(path, width, height, nrChannels, 4);
            if (data != null) {
                int w = width.get(0);
                int h = height.get(0);
                int levels = (int) Math.min(log2(w), log2(h));
                glTexStorage2D(GL_TEXTURE_2D, levels, texture.internalFormat, w, h);
                glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, texture.abstractFormat, texture.type, data);
                glGenerateMipmap(GL_TEXTURE_2D);
                stbi_image_free(data);
            } else {
                System.err.printf("Failed to load texture %s\n", path);
                glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, 0, 0);
            }
        }
    }

    private double log2(int w) {
        return (Math.log(w) / Math.log(2));
    }

    /**
     * Texture class for initialization
     */
    public static class Texture {
        public String name;
        public int min = GL_LINEAR_MIPMAP_LINEAR;
        public int mag = GL_LINEAR;
        public int sWrap = GL_REPEAT;
        public int tWrap = GL_REPEAT;
        public int internalFormat = GL_RGBA8;
        public int abstractFormat = GL_RGBA;
        public int type = GL_UNSIGNED_BYTE;

        /**
         * Calls {@link Texture#Texture(String, String)} with path "assets/textures"
         */
        public Texture(String name) {
            this("assets/textures", name);
        }

        /**
         * @param path Project relative path for texture location
         * @param name Name of file with type (.png, .jpg, etc.).
         */
        public Texture(String path, String name) {
            this.name = path + "/" + name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Texture texture = (Texture) o;
            // Should distinguish textures based on format
            return min == texture.min && mag == texture.mag && sWrap == texture.sWrap
                    && tWrap == texture.tWrap && internalFormat == texture.internalFormat
                    && abstractFormat == texture.abstractFormat && type == texture.type
                    && name.equals(texture.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, min, mag, sWrap, tWrap, internalFormat, abstractFormat, type);
        }
    }
}
