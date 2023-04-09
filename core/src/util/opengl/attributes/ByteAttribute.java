package util.opengl.attributes;

import util.opengl.VertexAttribute;

import static org.lwjgl.opengl.GL11.GL_BYTE;

/**
 * Byte, mapped to [-1, 1] (i.e, normalized)
 */
public class ByteAttribute extends VertexAttribute {
    public byte[] data;

    public ByteAttribute(int size, byte[] data, boolean dynamic) {
        this.size = size;
        this.type = GL_BYTE;
        this.normalized = true;
        this.count = data.length / size;
        this.data = data;
        this.dynamic = dynamic;
        this.sizeof = data.length;
    }
}
