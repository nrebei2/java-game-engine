package util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.util.BitSet;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Interface to input functions.
 */
public final class Input {
    // The window handle assigned to this input
    private long handle;

    // Pressed keyboard keys
    private BitSet pressedButtons = new BitSet(GLFW_KEY_LAST);
    private final BitSet justPressedButtons = new BitSet(GLFW_KEY_LAST);

    /**
     * Screen window origin at bottom left corner.
     */
    private int mouseX, mouseY;

    /**
     * The difference (in pixels) between the current pointer location and the last pointer location
     */
    private float deltaX, deltaY = 0;
    private int[] dimensions;

    private float scrollX, scrollY = 0;

    private final int count = 0;

    public Input() {
    }

    private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (window != handle || key == GLFW_KEY_UNKNOWN) return;
            switch (action) {
                case GLFW_RELEASE -> pressedButtons.set(key, false);
                case GLFW_PRESS -> {
                    pressedButtons.set(key, true);
                    justPressedButtons.set(key, true);
                }
            }
        }
    };

    private final GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            if (window != handle) return;
            scrollX = (float) xoffset;
            scrollY = (float) yoffset;
        }
    };

    /**
     * @param handle     of current window to set this class to listen input events from
     * @param dimensions 2 element (width, height) array holding screen dimensions
     */
    public void setWindow(long handle, int[] dimensions) {
        this.dimensions = dimensions;
        this.handle = handle;
        pressedButtons = new BitSet(GLFW_KEY_LAST);
        GLFW.glfwSetKeyCallback(handle, keyCallback);
        GLFW.glfwSetScrollCallback(handle, scrollCallback);
    }

    /**
     * Update attributes of the class
     */
    public void update() {
        // Reset data
        scrollX = scrollY = 0;
        justPressedButtons.clear();
        // Update mouse position and delta
        try (MemoryStack stack = stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1); // int*
            DoubleBuffer y = stack.mallocDouble(1); // int*
            glfwGetCursorPos(handle, x, y);
            int ypos = getScreenHeight() - (int) y.get(0);
            int xpos = (int) x.get(0);
            deltaX = (float) (xpos - mouseX);
            deltaY = (float) (ypos - mouseY);
            mouseY = ypos;
            mouseX = xpos;
        }
    }

    public int getScreenWidth() {
        return dimensions[0];
    }

    public int getScreenHeight() {
        return dimensions[1];
    }

    /**
     * @param key GLFW key-code
     * @return whether the key is currently held down (pressed)
     */
    public boolean isKeyPressed(int key) {
        return pressedButtons.get(key);
    }

    /**
     * @param key GLFW key-code
     * @return whether the key has just been pressed
     */
    public boolean keyJustPressed(int key) {
        return justPressedButtons.get(key);
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

    public float getScrollX() {
        return scrollX;
    }

    public float getScrollY() {
        return scrollY;
    }

    public boolean isButtonPressed(int button) {
        return glfwGetMouseButton(handle, button) == GLFW_PRESS;
    }
}
