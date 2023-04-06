package util;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.GL_FLOAT;

/**
 * Abstraction of a vertex attribute passed into some location in a vertex shader
 */
public abstract class VertexAttribute {
    public int count;
    public int size;
    public int type;
    public boolean normalized;
    public ByteBuffer data;

    public enum Type {
        FLOAT, INT, BYTE
    }

    public VertexAttribute(int size, float[] data) {
        this.size = size;
        this.type = GL_FLOAT;
        this.normalized = false;
        this.count = data.length / size;
        this.data = BufferUtils.createByteBuffer(data.length * 4);
        this.data.asFloatBuffer().put(data);
    }
}
