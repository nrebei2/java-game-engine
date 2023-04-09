package util.opengl;

import util.GameEngine;
import util.Matrix4;
import util.UnorderedList;
import util.ecs.Identifiable;
import util.opengl.TextureManager.Texture;

import static org.lwjgl.opengl.GL43.*;

public class Material {
    /**
     * List of textures on this material.
     */
    UnorderedList<TexInfo> texs;

    /**
     * Shader of this material
     */
    ShaderProgram shader;

    String shaderName;

    boolean dirty;

    /**
     * Create a new material
     *
     * @param shaderName For {@link ShaderProgram#ShaderProgram(String)}
     */
    public Material(String shaderName) {
        this();
        setShader(shaderName);
    }

    /**
     * Create a new material
     */
    public Material() {
        this.texs = new UnorderedList<>();
        this.dirty = false;
    }

    /**
     * Set the shader on this material
     *
     * @param shaderName For {@link ShaderProgram#ShaderProgram(String)}
     * @return this material for chaining
     */
    public Material setShader(String shaderName) {
        this.shader = GameEngine.shaderManager.getProgram(shaderName);
        this.shaderName = shaderName;
        this.dirty = true;
        return this;
    }

    /**
     * Add a texture to this material. It is safe to add a texture anytime.
     *
     * @param texture     Texture this material will hold
     * @param uniformName Name of uniform in shader that will hold this texture.
     * @return this material for chaining
     */
    public Material addTexture(Texture texture, String uniformName) {
        int id = GameEngine.textureManager.getTexture(texture);
        texs.add(new TexInfo(id, uniformName));
        return this;
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

}
