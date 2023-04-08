package util.opengl;

import util.Matrix4;
import util.opengl.attributes.ByteAttribute;
import util.opengl.attributes.FloatAttribute;
import util.opengl.attributes.IntAttribute;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.lwjgl.opengl.GL43.*;

/**
 * A mesh component consists of vertices (held by its geometry) and a shader.
 */
public class Mesh {
    private int VAO;

    /**
     * Geometry currently assigned to this object
     */
    private Geometry geo;

    /**
     * Material currently assigned to this object
     */
    private Material mat;

    public Mesh(Material material) {
        VAO = glGenVertexArrays();
        mat = material;
    }

    public void setGeometry(Geometry geo) {
        this.geo = geo;

        // TODO: This should be done every time the geometry changes (attributes added) or the shader in the material changes
        // TODO: Try-hard optimizations:
        //  - If the geometry is static it would probably be best to batch all the attributes in one VBO
        //  - Could buffer entire meshes with the same vertex format to reduce AttribPointer calls
        glBindVertexArray(VAO);
        for (Map.Entry<String, VertexAttribute> entry : geo.attributeMap.entrySet()) {
            // Check if shader attribute and geometry attribute match names
            String name = entry.getKey();
            VertexAttribute attribute = entry.getValue();
            if (!mat.shader.attributes.containsKey(name)) continue;
            int location = mat.shader.attributes.get(name);

            // Generate VBO, set attrib pointer
            int VBO = glGenBuffers();
            //System.out.printf("%s location: %d\n", name, program.attributes.get(name));
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            switch (attribute.type) {
                case GL_FLOAT -> {
                    FloatAttribute attr = (FloatAttribute) attribute;
                    glBufferData(GL_ARRAY_BUFFER, attr.data, attribute.dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
                }
                case GL_UNSIGNED_INT -> {
                    IntAttribute attr = (IntAttribute) attribute;
                    glBufferData(GL_ARRAY_BUFFER, attr.data, attribute.dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
                }
                case GL_UNSIGNED_BYTE -> {
                    ByteAttribute attr = (ByteAttribute) attribute;
                    // TODO: lwjgl supports only direct buffers so wrap will not work :(
                    glBufferData(GL_ARRAY_BUFFER, ByteBuffer.wrap(attr.data), attribute.dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
                }
            }
            glVertexAttribPointer(location, attribute.size, attribute.type, attribute.normalized, 0, 0);
            glEnableVertexAttribArray(location);
        }

        if (geo.indices != null) {
            int EBO = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, geo.indices, GL_STATIC_DRAW);
        }

        glBindVertexArray(0);
    }

    public Geometry getGeo() {
        return geo;
    }

    public Material getMat() {
        return mat;
    }

    /**
     * Binds the shader on this mesh, then renders the mesh onto the screen.
     */
    public void render() {
        if (geo == null) return;

        mat.shader.bind();

        // Bind textures from material
        int i = 0;
        for (Material.TexInfo tex : mat.texs) {
            if (mat.shader.uniforms.containsKey(tex.uniformName)) {
                //System.out.printf("Binding %s\n", tex.uniformName);
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(GL_TEXTURE_2D, tex.id);
                mat.shader.setInt(tex.uniformName, i);
            }
            i += 1;
        }

        // Draw
        glBindVertexArray(VAO);
        if (geo.indices != null) {
            glDrawElements(GL_TRIANGLES, geo.indices.length, GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, geo.count());
        }
        glBindVertexArray(0);
    }

    /**
     * Sets of view-projection matrix for rendering of this mesh.
     */
    public void setCombinedMatrix(Matrix4 mat) {
        this.mat.shader.setMat4("u_viewProj", mat);
    }

    /**
     * Sets the model matrix for this mesh.
     */
    public void setModelMatrix(Matrix4 model) {
        mat.shader.setMat4("u_model", model);
    }
}
