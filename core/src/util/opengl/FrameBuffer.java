package util.opengl;

import util.GameEngine;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

/**
 * Represents an OpenGL Framebuffer object
 */
public class FrameBuffer {

    public static class BufferOptions {
        public enum AttachOption {
            NONE, READ, NO_READ
        }
        AttachOption color = AttachOption.READ;
        AttachOption depth = AttachOption.NO_READ;
        AttachOption stencil = AttachOption.NO_READ;
    }

    /**
     * FBO name
     */
    int fbo;

    /**
     * texture ids, if they exist
     */
    int colorTexture, depthTexture, stencilTexture = -1;

    /**
     * Creates a Framebuffer object with a color, depth, and stencil attachment. Note only the color attachment is readable.
     * The width and height are FIXED, if the resolution changes it may be best to create a new FrameBuffer.
     *
     * @param width  width of buffer
     * @param height height of buffer
     */
    public FrameBuffer(int width, int height) {
        this(width, height, new BufferOptions());
    }

    public FrameBuffer(int width, int height, BufferOptions options) {
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // Color attachment
        if (options.color == BufferOptions.AttachOption.NONE) {
            glDrawBuffer(GL_NONE);
            glReadBuffer(GL_NONE);
        } else {
            attachBuffer(width, height, GL_COLOR_ATTACHMENT0, options.color == BufferOptions.AttachOption.READ);
        }

        // Depth/Stencil attachment
        if (options.depth == options.stencil) {
            if (options.depth != BufferOptions.AttachOption.NONE)
                attachBuffer(width, height, GL_DEPTH_STENCIL_ATTACHMENT, options.depth == BufferOptions.AttachOption.READ);
        } else {
            if (options.depth != BufferOptions.AttachOption.NONE)
                attachBuffer(width, height, GL_DEPTH_ATTACHMENT, options.depth == BufferOptions.AttachOption.READ);
            if (options.stencil != BufferOptions.AttachOption.NONE)
                attachBuffer(width, height, GL_STENCIL_ATTACHMENT, options.stencil == BufferOptions.AttachOption.READ);
        }

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FrameBuffer failed to initialize with error " + glCheckFramebufferStatus(GL_FRAMEBUFFER));
        }

        unbind();
    }

    /**
     * Calls {@link #FrameBuffer(int, int)} with current screen width and height
     */
    public FrameBuffer() {
        this(GameEngine.input.getScreenWidth(), GameEngine.input.getScreenHeight());
    }

    private void attachBuffer(int width, int height, int attachment, boolean readable) {
        // internal format
        int internal = switch (attachment) {
            case GL_COLOR_ATTACHMENT0 -> GL_RGB;
            case GL_DEPTH_ATTACHMENT -> GL_DEPTH_COMPONENT;
            case GL_STENCIL_ATTACHMENT -> GL_STENCIL_INDEX8;
            case GL_DEPTH_STENCIL_ATTACHMENT -> GL_DEPTH24_STENCIL8;
            default -> throw new IllegalStateException("Unexpected value: " + attachment);
        };

        if (readable) {
            // texture
            int type = switch (attachment) {
                case GL_COLOR_ATTACHMENT0, GL_STENCIL_ATTACHMENT -> GL_UNSIGNED_BYTE;
                case GL_DEPTH_ATTACHMENT -> GL_FLOAT;
                case GL_DEPTH_STENCIL_ATTACHMENT -> GL_UNSIGNED_INT_24_8;
                default -> throw new IllegalStateException("Unexpected value: " + attachment);
            };
            int format = switch (attachment) {
                case GL_COLOR_ATTACHMENT0 -> GL_RGB;
                case GL_DEPTH_ATTACHMENT -> GL_DEPTH_COMPONENT;
                case GL_STENCIL_ATTACHMENT -> GL_STENCIL_INDEX;
                case GL_DEPTH_STENCIL_ATTACHMENT -> GL_DEPTH_STENCIL;
                default -> throw new IllegalStateException("Unexpected value: " + attachment);
            };
            int texture = glGenTextures();
            switch (attachment) {
                case GL_COLOR_ATTACHMENT0 -> colorTexture = texture;
                case GL_DEPTH_ATTACHMENT -> depthTexture = texture;
                case GL_STENCIL_ATTACHMENT -> stencilTexture = texture;
                case GL_DEPTH_STENCIL_ATTACHMENT -> depthTexture = texture;
            };
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexImage2D(
                    GL_TEXTURE_2D, 0, internal, width, height, 0,
                    format, type,
                    (ByteBuffer) null
            );
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);
            glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texture, 0);
        } else {
            // Renderbuffer
            int rbo = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, rbo);
            glRenderbufferStorage(GL_RENDERBUFFER, internal, width, height);
            glBindRenderbuffer(GL_RENDERBUFFER, 0);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, rbo);
        }
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
