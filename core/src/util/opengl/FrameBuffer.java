package util.opengl;

import util.GameEngine;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

/** Represents an OpenGL Framebuffer object */
public class FrameBuffer {

    /** FBO name */
    int fbo;

    /** Color texture id */
    int texture;

    /**
     * Creates a Framebuffer object with a color, depth, and stencil attachment. Note only the color attachment is readable.
     * @param width width of buffer
     * @param height height of buffer
     */
    public FrameBuffer(int width, int height) {
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        System.out.println(width);
        System.out.println(height);

        // Color attachment
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        // Depth/Stencil attachment
        // Renderbuffer -> not readable
        int rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
           System.err.println("FrameBuffer failed to initialize!");
        }

        unbind();
    }

    /**
     * Calls {@link #FrameBuffer(int, int)} with current screen width and height
     */
    public FrameBuffer() {
        this(GameEngine.input.getScreenWidth(), GameEngine.input.getScreenHeight());
    }

    /**
     * Binds the framebuffer. Any subsequent drawing calls will use this framebuffer.
     */
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // Most likely calling in the middle of the render loop
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    /**
     * Unbinds the framebuffer. Any subsequent drawing calls will use the default buffer.
     */
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
