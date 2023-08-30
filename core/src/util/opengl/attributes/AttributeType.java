package util.opengl.attributes;
import static org.lwjgl.opengl.GL11.*;

public enum AttributeType {
    FLOAT, INT, BYTE, MAT4;

    public int toGLType() {
        switch (this) {
            case INT:
                return GL_INT;
            case BYTE:
                return GL_BYTE;
            case FLOAT:
            case MAT4:
            default:
                return GL_FLOAT;
        }
    }
}
