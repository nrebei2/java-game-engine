package game.components;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import util.Geometry;
import util.Matrix4;
import util.ShaderProgram;
import util.VertexAttribute;

import java.util.Map;

/**
 * A mesh component consists of vertices (held by its geometry) and a shader.
 */
public class Mesh {
    private ShaderProgram program;
    private int VAO;
    private Geometry geo;

    public Mesh(ShaderProgram program) {
        VAO = GL30.glGenVertexArrays();
        this.program = program;
    }

    public void setGeometry(Geometry geo) {
        this.geo = geo;
        GL30.glBindVertexArray(VAO);
        for (Map.Entry<String, VertexAttribute> entry: geo.attributeMap.entrySet()) {
            // Check if shader attribute and geometry attribute match names
            String name = entry.getKey();
            VertexAttribute attribute = entry.getValue();
            if (!program.attributes.containsKey(name)) continue;
            int location = program.attributes.get(name);

            // Generate VBO, set attrib pointer
            int VBO = GL15.glGenBuffers();
            //System.out.printf("%s location: %d\n", name, program.attributes.get(name));
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, attribute.data, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(location, attribute.size, attribute.type, attribute.normalized, 0, 0);
            GL20.glEnableVertexAttribArray(location);
        }

        if (geo.indices != null) {
            int EBO = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, geo.indices, GL15.GL_STATIC_DRAW);
        }

        GL30.glBindVertexArray(0);
    }

    /**
     * Binds the shader on this mesh, then renders the mesh onto the screen.
     */
    public void render() {
        if (geo == null) return;

        program.bind();
        GL30.glBindVertexArray(VAO);
        if (geo.indices != null) {
            GL11.glDrawElements(GL11.GL_TRIANGLES, geo.indices.length, GL11.GL_UNSIGNED_INT, 0);
        }
        else {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, geo.count());
        }
        GL30.glBindVertexArray(0);
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
    public void setModelMatrix(Matrix4 model) {  program.setMat4("u_model", model);  }
}
