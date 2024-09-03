package util.opengl;

import util.Matrix4;
import util.opengl.attributes.AttributeType;
import util.opengl.attributes.ByteAttribute;
import util.opengl.attributes.FloatAttribute;
import util.opengl.attributes.IntAttribute;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

/**
 * A mesh component consists of vertices (held by its geometry) and a shader.
 */
public class Mesh {
    // OpenGL objects
    private int VBO;
    private final int VAO;

    /**
     * Geometry currently assigned to this mesh
     */
    private Geometry geo;

    /**
     * Material currently assigned to this mesh
     */
    private Material mat;

    public Mesh() {
        VAO = glGenVertexArrays();
    }

    /**
     * Set the material of this mesh.
     *
     * @param mat Material holding shader and textures.
     * @return This mesh for chaining
     */
    public Mesh setMat(Material mat) {
        this.mat = mat;

        if (this.geo != null && mat.shader != null) {
            // Attribute locations most likely changed
            glBindVertexArray(VAO);
            // Critical
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            updateAttribPointers();
            glBindVertexArray(0);
            mat.dirty = false;
        }
        // If the geometry is null, to render setGeometry must be called
        // mat != null -> buffers and pointers will be set

        return this;
    }

    /**
     * Sets the geometry of this mesh.
     * The mesh will respond to new attributes and if any attributes' data are changed.
     *
     * @param geo Geometry holding attribute info
     * @return This mesh object for chaining
     */
    public Mesh setGeometry(Geometry geo) {
        this.geo = geo;

        // TODO: Try-hard optimizations:
        //  - Could buffer entire meshes with the same vertex format to reduce AttribPointer calls
        glBindVertexArray(VAO);

        // Generate VBO, set attrib pointers
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, geo.offset, geo.dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

        // Fill buffer
        for (Geometry.AttrInfo entry : geo.attributes) {
            fillBuffer(entry);
        }

        // Set attribute pointers
        if (mat != null && mat.shader != null) updateAttribPointers();
        // If the material is null, to render geometry setMat must be called
        // geo != null -> buffers and pointers will be set

        if (geo.indices != null) {
            int EBO = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, geo.indices, GL_STATIC_DRAW);
        }

        glBindVertexArray(0);

        geo.dirty = false;
        return this;
    }

    /**
     * Required when attribute data is changed
     */
    private void updateBuffers() {
        // Bind VBO if needed and only once
        boolean bind = false;
        for (Geometry.AttrInfo entry : geo.attributes) {
            if (!entry.dirty) continue;
            if (!bind) {
                glBindBuffer(GL_ARRAY_BUFFER, VBO);
                bind = true;
            }
            fillBuffer(entry);
        }
    }

    // Helper
    private void fillBuffer(Geometry.AttrInfo entry) {
        VertexAttribute attribute = entry.attribute;
        switch (attribute.type) {
            case FLOAT, MAT4 -> {
                FloatAttribute attr = (FloatAttribute) attribute;
                glBufferSubData(GL_ARRAY_BUFFER, entry.offset, attr.data);
            }
            case INT -> {
                IntAttribute attr = (IntAttribute) attribute;
                glBufferSubData(GL_ARRAY_BUFFER, entry.offset, attr.data);
            }
            case BYTE -> {
                ByteAttribute attr = (ByteAttribute) attribute;
                // TODO: lwjgl supports only direct buffers so wrap will not work :(
                glBufferSubData(GL_ARRAY_BUFFER, entry.offset, ByteBuffer.wrap(attr.data));
            }
        }
        entry.dirty = false;
    }

    private void putAttr(String name, int locOffset, int size, AttributeType type, boolean normalized, int stride, int pointer, boolean instanced) {
        if (!mat.shader.attributes.containsKey(name)) {
            System.err.printf("Material's current shader %s has no attribute of name %s!\n", mat.shaderName, name);
            return;
        }
        int location = mat.shader.attributes.get(name);
        glEnableVertexAttribArray(location+locOffset);
        glVertexAttribPointer(
                location + locOffset,
                size, type.toGLType(), normalized, stride,
                pointer
        );
        if (instanced) {
            glVertexAttribDivisor(location+locOffset, 1);
        }
    }

    /**
     * Required when the Material is changed
     */
    private void updateAttribPointers() {
        for (Geometry.AttrInfo entry : geo.attributes) {
            // Fill buffer
            VertexAttribute attribute = entry.attribute;
            String name = entry.name;

            // pointer points to offset supplied in subdata call
            if (attribute.type != AttributeType.MAT4) {
                // Check if shader attribute and geometry attribute match names
                putAttr(name, 0, attribute.size, attribute.type, attribute.normalized, 0, entry.offset, attribute.instanced);
            } else {
                // Mat4
                putAttr(name, 0, attribute.size, attribute.type, attribute.normalized, 64, entry.offset, attribute.instanced);
                putAttr(name, 1, attribute.size, attribute.type, attribute.normalized, 64, entry.offset + 16, attribute.instanced);
                putAttr(name, 2, attribute.size, attribute.type, attribute.normalized, 64, entry.offset + 32, attribute.instanced);
                putAttr(name, 3, attribute.size, attribute.type, attribute.normalized, 64, entry.offset + 48, attribute.instanced);
            }
        }
    }

    public Geometry getGeo() {
        return geo;
    }

    public Material getMat() {
        return mat;
    }

    /**
     * Begin rendering sequence for this mesh. You must call this method before setting shader uniforms!
     */
    public void begin() {
        if (mat == null || mat.shader == null) {
            System.err.println("In Mesh#begin: Cannot begin without material set with shader!");
            return;
        }
        mat.shader.bind();
    }

    private void beginRender() {
        if (geo == null || mat == null || mat.shader == null) {
            System.err.println("In Mesh#render: Cannot render without geometry and material set with shader!");
            return;
        }

        // Hack kinda
        if (geo.dirty) setGeometry(geo);
        if (mat.dirty) setMat(mat);

        // If we batch meshes together, would need to only set materials and bind VAO once
        //  though at that point instancing would be the better option
        // Bind textures from material
        int i = 0;
        for (Material.TexInfo tex : mat.texs) {
            if (mat.shader.uniforms.containsKey(tex.uniformName)) {
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(tex.target, tex.id);
                mat.setInt(tex.uniformName, i);
            }
            i += 1;
        }

        // Draw
        glBindVertexArray(VAO);
        updateBuffers();
    }

    /**
     * Renders the mesh onto the current framebuffer.
     *
     * @param primitive the kind of primitives being constructed
     */
    public void render(int primitive) {
        beginRender();
        if (geo.indices != null) {
            glDrawElements(primitive, geo.indices.length, GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(primitive, 0, geo.count());
        }
        // No need
        //glBindVertexArray(0);
    }

    /**
     * Renders the mesh onto the current framebuffer as triangles.
     *
     * @param count the number of instances to render
     */
    public void renderInstanced(int count) {
        beginRender();
        if (geo.indices != null) {
            glDrawElementsInstanced(GL_TRIANGLES, geo.indices.length, GL_UNSIGNED_INT, 0, count);
        } else {
            glDrawArraysInstanced(GL_TRIANGLES, 0, geo.count(), count);
        }
    }

    /**
     * Renders the mesh onto the current framebuffer as triangles.
     */
    public void render() {
        render(GL_TRIANGLES);
    }

    /**
     * End the drawing sequence of this mesh.
     */
    public void end() {
    }

    /**
     * Sets of view-projection matrix for rendering of this mesh. Call begin before this method.
     */
    public void setCombinedMatrix(Matrix4 mat) {
        this.mat.setMat4("u_viewProj", mat);
    }

    /**
     * Sets the model matrix for this mesh. Call begin before this method.
     */
    public void setModelMatrix(Matrix4 model) {
        mat.setMat4("u_model", model);
    }
}
