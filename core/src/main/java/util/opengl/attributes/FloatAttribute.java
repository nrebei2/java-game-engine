package util.opengl.attributes;

import util.Matrix4;
import util.opengl.VertexAttribute;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class FloatAttribute extends VertexAttribute {
    public float[] data;

    public FloatAttribute(int size, float[] data) {
        this(size, data, false, false);
    }

    public FloatAttribute(int size, float[] data, boolean dynamic) {
        this(size, data, dynamic, false);
    }

    public FloatAttribute(int size, float[] data, boolean dynamic, boolean instanced) {
        this.size = size;
        this.type = AttributeType.FLOAT;
        this.normalized = false;
        this.data = data;
        this.dynamic = dynamic;
        this.sizeof = data.length * 4;
        this.instanced = instanced;
        this.count = instanced ? 0 : data.length / size;
    }

    public FloatAttribute(Matrix4[] data) {
        this(data, false, false);
    }

    public FloatAttribute(Matrix4[] data, boolean dynamic) {
        this(data, dynamic, false);
    }

    public FloatAttribute(Matrix4[] data, boolean dynamic, boolean instanced) {
        this.size = 4;
        this.type = AttributeType.MAT4;
        this.normalized = false;

        // push into data
        this.data = new float[data.length * 16];
        int i = 0;
        for (Matrix4 mat : data) {
            for (int j = i * 16; j < (i + 1) * 16; j++) {
                this.data[j] = mat.val[j % 16];
            }
            i += 1;
        }

        this.dynamic = dynamic;
        this.sizeof = data.length * 16 * 4;
        this.instanced = instanced;
        this.count = instanced ? 0 : data.length;
    }
}
