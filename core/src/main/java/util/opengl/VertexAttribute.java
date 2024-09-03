package util.opengl;

import util.opengl.attributes.AttributeType;

/**
 * Abstraction of a vertex attribute passed into some location in a vertex shader
 */
public abstract class VertexAttribute {
    /**
     * Number of distinct vertices in this attribute
     */
    public int count;
    /**
     * Number of components per attribute
     */
    public int size;
    /**
     * OpenGL data type for each element
     */
    public AttributeType type;
    /**
     * Whether to normalize ([-1, -1] signed, [0, 1] unsigned)
     */
    public boolean normalized;
    /**
     * Whether the data in this attribute will change once set
     */
    public boolean dynamic;
    /**
     * Size of data in bytes
     */
    public int sizeof;

    /**
     * Whether each attribute should correspond to an instance instead of a vertex
     */
    public boolean instanced;
}
