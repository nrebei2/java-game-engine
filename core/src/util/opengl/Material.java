package util.opengl;

import org.lwjgl.system.MemoryStack;
import util.Matrix4;
import util.UnorderedList;
import util.ecs.Identifiable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Material {
    /**
     * List of textures on this material.
     * <p>
     * TODO: Ideally this should be an IntMap< String>
     */
    public UnorderedList<TexInfo> texs;

    /**
     * Shader of this material
     */
    public ShaderProgram shader;

    String shaderName;

    /**
     * @param shaderName For {@link ShaderProgram#ShaderProgram(String)}
     * @param textures   Textures this material will maintain
     */
    public Material(String shaderName, Texture... textures) {
        this.shader = ShaderManager.getInstance().getProgram(shaderName);
        this.texs = new UnorderedList<>();
        this.shaderName = shaderName;

        addTextures(textures);
    }

    /**
     * Add a texture to this material. It is safe to add a texture anytime.
     *
     * @param textures Textures this material will maintain
     */
    public void addTextures(Texture... textures) {
        int[] ids = new int[textures.length];
        glGenTextures(ids);
        for (int i = 0; i < textures.length; i++) {
            loadTex(ids[i], textures[i]);
            texs.add(new TexInfo(ids[i], textures[i].uniformName));
        }
    }

    /**
     * Adds a Framebuffer's color attachment as a texture.
     *
     * @param buffer      FBO holding color attachment
     * @param uniformName Name of uniform in shader that will hold this texture.
     */
    public void addFBOColorTex(FrameBuffer buffer, String uniformName) {
        texs.add(new TexInfo(buffer.texture, uniformName));
    }

    /**
     * Adds a Skybox's cube map as a texture.
     *
     * @param map         Skybox holding cubemap texture
     * @param uniformName Name of uniform in shader that will hold this texture.
     */
    public void addSkyboxTex(Skybox map, String uniformName) {
        texs.add(new TexInfo(map.texture, uniformName));
    }

    /**
     * Set the value of a uniform variable for the current attached shader
     *
     * @param name  Name of uniform on shader
     * @param value value to set as
     */
    public void setBool(String name, boolean value) {
        if (!shader.uniforms.containsKey(name)) return;
        glUniform1f(shader.uniforms.get(name), value ? 1 : 0);
    }

    /**
     * Set the value of a uniform variable for the current attached shader
     *
     * @param name  Name of uniform on shader
     * @param value value to set as
     */
    public void setInt(String name, int value) {
        if (!shader.uniforms.containsKey(name)) return;
        glUniform1i(shader.uniforms.get(name), value);
    }

    /**
     * Set the value of a uniform variable for the current attached shader
     *
     * @param name  Name of uniform on shader
     * @param value value to set as
     */
    public void setFloat(String name, float value) {
        if (!shader.uniforms.containsKey(name)) return;
        glUniform1f(shader.uniforms.get(name), value);
    }

    /**
     * Set the value of a uniform variable for the current attached shader
     *
     * @param name Name of uniform on shader
     * @param mat  value to set as
     */
    public void setMat4(String name, Matrix4 mat) {
        if (!shader.uniforms.containsKey(name)) return;
        glUniformMatrix4fv(shader.uniforms.get(name), false, mat.val);
    }

    /**
     * @param uniformName Texture to remove
     */
    public void removeTexture(String uniformName) {
        var iter = texs.iterator();
        while (iter.hasNext()) {
            TexInfo i = iter.next();
            if (i.uniformName.equals(uniformName)) {
                iter.remove();
                break;
            }
        }
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
            String path = "assets/textures/" + texture.name;
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
     * Texture class for storage
     */
    static class TexInfo extends Identifiable {
        public int id;
        public String uniformName;

        /**
         * @param id          GL shader id
         * @param uniformName Name of uniform in shader that will hold this texture.
         */
        public TexInfo(int id, String uniformName) {
            this.id = id;
            this.uniformName = uniformName;
        }
    }

    /**
     * Texture class for initialization
     */
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
         * @param name        Name of file with type (.png, .jpg, etc.). Assumed to be located in assets/textures.
         * @param uniformName Name of uniform in shader that will hold this texture.
         */
        public Texture(String name, String uniformName) {
            this.name = name;
            this.uniformName = uniformName;
        }
    }
}