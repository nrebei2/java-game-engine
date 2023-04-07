package util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.BitSet;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

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
    private int mouseX, mouseY;

    /**
     * The difference (in pixels) between the current pointer location and the last pointer location
     */
    private float deltaX, deltaY = 0;
    private IntBuffer dimensions;

    public Input() {
    }

    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (window != handle || key == GLFW_KEY_UNKNOWN) return;
            switch (action) {
                case GLFW_RELEASE -> pressedButtons.set(key, false);
                case GLFW_PRESS -> pressedButtons.set(key, true);
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
            mouseY = (int) ypos;
            mouseX = (int) xpos;
        }
    };

    /**
     * @param handle     of current window to set this class to listen input events from
     * @param dimensions 2 element (width, height) buffer holding screen dimensions
     */
    public void setWindow(long handle, IntBuffer dimensions) {
        this.dimensions = dimensions;
        this.handle = handle;
        pressedButtons = new BitSet(GLFW_KEY_LAST);
        GLFW.glfwSetKeyCallback(handle, keyCallback);
        GLFW.glfwSetScrollCallback(handle, scrollCallback);
        GLFW.glfwSetCursorPosCallback(handle, cursorPosCallback);

        // Set initial cursor position
        try (MemoryStack stack = stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1); // int*
            DoubleBuffer y = stack.mallocDouble(1); // int*
            glfwGetCursorPos(handle, x, y);
            this.mouseX = (int) x.get(0);
            this.mouseY = getScreenHeight() - (int) y.get(0);
        }
    }

    /**
     * Prepare attributes for next frame.
     */
    public void prepareNext() {
        deltaX = 0;
        deltaY = 0;

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
