package util;

/**
 * Abstraction of a vertex attribute passed into some location in a vertex shader
 */
public abstract class VertexAttribute {
    public int count;
    public int size;
    public int type;
    public boolean normalized;
    public boolean dynamic;
}
