package util;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.nio.IntBuffer;

/**
 * Interface to input functions. Most functions are static.
 */
public final class Input {
    // The window handle assigned to this input
    private long handle;

    // Pressed keyboard keys
    private BitSet pressedButtons = new BitSet(GLFW_KEY_LAST);

    /**
     * Screen window origin at bottom left corner.
     */
    private float mouseX, mouseY;

    /**
     * The difference (in pixels) between the current pointer location and the last pointer location
     */
    private float deltaX, deltaY;
    private IntBuffer dimensions;

    public Input() {}

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
            // invert ypos so y is relative to bottom edge
            ypos = getScreenHeight() - ypos;

            deltaX = (float) (xpos - mouseX);
            deltaY = (float) (ypos - mouseY);
            mouseY = (float) ypos;
            mouseX = (float) xpos;
        }
    };

    /**
     * @param handle of current window to set this class to listen input events from
     * @param dimensions 2 element (width, height) buffer holding screen dimensions
     */
    public void setWindow(long handle, IntBuffer dimensions) {
        this.dimensions = dimensions;
        pressedButtons = new BitSet(GLFW_KEY_LAST);
        GLFW.glfwSetKeyCallback(handle, keyCallback);
        GLFW.glfwSetScrollCallback(handle, scrollCallback);
        GLFW.glfwSetCursorPosCallback(handle, cursorPosCallback);
    }

    public int getScreenWidth() {
        return dimensions.get(0);
    }

    public int getScreenHeight() {
        return dimensions.get(1);
    }

    /**
     * @param key GLFW key-code
     * @return whether the key is currently held down (pressed)
     */
    public boolean isKeyPressed(int key) {
        return pressedButtons.get(key);
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public boolean isButtonPressed(int button) {
        return glfwGetMouseButton(handle, button) == GLFW_PRESS;
    }
}
