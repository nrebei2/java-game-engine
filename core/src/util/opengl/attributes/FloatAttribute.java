package util.opengl.attributes;

import util.opengl.VertexAttribute;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class FloatAttribute extends VertexAttribute {
    public float[] data;

    public FloatAttribute(int size, float[] data, boolean dynamic) {
        this.size = size;
        this.type = GL_FLOAT;
        this.normalized = false;
        this.count = data.length / size;
        this.data = data;
        this.dynamic = dynamic;
        this.sizeof = data.length * 4;
    }
}
