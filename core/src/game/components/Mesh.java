package game.components;

import util.Geometry;
import util.Matrix4;
import util.ShaderProgram;
import util.VertexAttribute;
import util.attributes.ByteAttribute;
import util.attributes.FloatAttribute;
import util.attributes.IntAttribute;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.lwjgl.opengl.GL43.*;

/**
 * A mesh component consists of vertices (held by its geometry) and a shader.
 */
public class Mesh {
    private ShaderProgram program;
    private int VAO;
    private Geometry geo;

    public Mesh(ShaderProgram program) {
        VAO = glGenVertexArrays();
        this.program = program;
    }

    public void setGeometry(Geometry geo) {
        this.geo = geo;
        glBindVertexArray(VAO);
        for (Map.Entry<String, VertexAttribute> entry : geo.attributeMap.entrySet()) {
            // Check if shader attribute and geometry attribute match names
            String name = entry.getKey();
            VertexAttribute attribute = entry.getValue();
            if (!program.attributes.containsKey(name)) continue;
            int location = program.attributes.get(name);

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

    /**
     * Binds the shader on this mesh, then renders the mesh onto the screen.
     */
    public void render() {
        if (geo == null) return;

        program.bind();
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
        program.setMat4("u_viewProj", mat);
    }

    /**
     * Sets the model matrix for this mesh.
     */
    public void setModelMatrix(Matrix4 model) {
        program.setMat4("u_model", model);
    }
}
