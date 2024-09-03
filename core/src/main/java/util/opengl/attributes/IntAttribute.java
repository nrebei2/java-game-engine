package util.opengl.attributes;

import util.opengl.VertexAttribute;

import static org.lwjgl.opengl.GL11.GL_INT;

public class IntAttribute extends VertexAttribute {
    public int[] data;

    public IntAttribute(int size, int[] data, boolean dynamic) {
        this.size = size;
        this.type = AttributeType.INT;
        this.normalized = false;
        this.count = data.length / size;
        this.data = data;
        this.dynamic = dynamic;
        this.sizeof = data.length * 4;
    }
}
