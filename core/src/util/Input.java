package util;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Interface to input functions. Most functions are static.
 */
public final class Input {
    // The window handle assigned to this input
    private long handle;

    // Pressed keyboard keys
    private BitSet pressedButtons = new BitSet(GLFW_KEY_LAST);

    private float mouseX, mouseY;
    private float deltaX, deltaY;

    private Input() {}

    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (window != handle || key == GLFW_KEY_UNKNOWN) return;
            switch (action) {
                case GLFW_RELEASE ->
                    pressedButtons.set(key, false);
                case GLFW_PRESS ->
                    pressedButtons.set(key, true);
            }
        }
    };

    private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            // TODO if needed
        }
    };

    private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            if (window != handle) return;

            deltaX = (float) (xpos - mouseX);
            deltaY = (float) (mouseY - ypos);
            mouseY = (float) ypos;
            mouseX = (float) xpos;

        }
    }

    public void setWindow(long handle) {
        pressedButtons = new BitSet(GLFW_KEY_LAST);
        GLFW.glfwSetKeyCallback(handle, keyCallback);
        GLFW.glfwSetScrollCallback(handle, scrollCallback);
        GLFW.glfwSetCursorPosCallback(handle, cursorPosCallback);
        GLFW.glfwSetMouseButtonCallback(handle, mouseButtonCallback);
    }
}
