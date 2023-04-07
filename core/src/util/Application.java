package util;

import game.Game;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Application {
    // The window handle
    private long window;

    // The current listener
    private Game listener;

    IntBuffer dimensions = BufferUtils.createIntBuffer(2);

    public Application(Game listener, Config config) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        this.listener = listener;

        init(config);
        loop();

        // Application will close
        listener.dispose();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(Config config) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, config.resizable ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable

        // Create the window
        // TODO Maybe because im just on linux, but setting monitor to NULL anytime will result in tearing
        window = glfwCreateWindow(
                config.width, config.height, config.title, glfwGetPrimaryMonitor(), NULL
        );
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Resize callback
        glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int nWidth, int nHeight) {
                dimensions.put(0, nWidth);
                dimensions.put(1, nHeight);
                listener.resize(nWidth, nHeight);
            }
        });

        glfwSetWindowIconifyCallback(window, new GLFWWindowIconifyCallback() {
            @Override
            public void invoke(long window, boolean iconified) {
                if (iconified) {
                    listener.pause();
                } else {
                    listener.resume();
                }
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            dimensions.put(0, pWidth.get(0));
            dimensions.put(1, pHeight.get(0));

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - dimensions.get(0)) / 2,
                    (vidmode.height() - dimensions.get(1)) / 2
            );
        } // the stack frame is popped automatically


        // Capture the mouse
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);

        // Make the window visible
        glfwShowWindow(window);

        // Update mouse cursor
        glfwPollEvents();

        // Create global input instance
        GameEngine.input = new Input();
        GameEngine.input.setWindow(window, dimensions);
    }

    private void loop() {
        // Critical
        GL.createCapabilities();

        glViewport(0, 0, GameEngine.input.getScreenWidth(), GameEngine.input.getScreenHeight());

        // Set the clear color
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        // Enable states
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        listener.create();

        double lastFrame = glfwGetTime();
        while (!glfwWindowShouldClose(window)) {
            GameEngine.input.update();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            double curFrame = glfwGetTime();
            listener.render((float) (curFrame - lastFrame));
            lastFrame = curFrame;

            glfwSwapBuffers(window); // swap the color buffers

            GameEngine.input.prepareNext();

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
}
