package util.opengl;

import org.lwjgl.system.MemoryStack;
import util.GameEngine;

import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Manages an OpenGL Program, consisting of shaders.
 * Vertex, fragment, geometry, and compute shader supported.
 */
public class ShaderProgram {
    /**
     * OpenGL ID for the managed program
     */
    int ID;
    /**
     * Map of uniform names to locations
     */
    public Map<String, Integer> uniforms = new HashMap<>();
    /**
     * Map of attribute names to locations
     */
    public Map<String, Integer> attributes = new HashMap<>();

    private String name;

    // Used for closure
    interface addShader {
        void addShader(int type, Path path);
    }

    /**
     * Creates a program. Will attempt to load shaders path/name.[vert, frag, geom, comp].
     *
     * @param name path of shader
     */
    public ShaderProgram(String name) {
        this.name = name;
        reload();
    }

    /**
     * (re)load shader from source
     */
    public void reload() {
        // Standard process
        ID = glCreateProgram();
        Path vertPath = Paths.get(name + ".vert");
        Path fragPath = Paths.get(name + ".frag");
        Path geoPath = Paths.get(name + ".geom");
        Path compPath = Paths.get(name + ".comp");

        // Attached shader ids
        List<Integer> sIDS = new ArrayList<>();

        addShader a = (t, p) -> {
            Optional<Integer> sID = loadShader(t, p);
            sID.ifPresent((id -> {
                glAttachShader(ID, id);
                sIDS.add(id);
            }));
        };

        a.addShader(GL_VERTEX_SHADER, vertPath);
        a.addShader(GL_FRAGMENT_SHADER, fragPath);
        a.addShader(GL_GEOMETRY_SHADER, geoPath);
        a.addShader(GL_COMPUTE_SHADER, compPath);
        glLinkProgram(ID);

        // Check if compilation succeeded
        try (MemoryStack stack = stackPush()) {
            IntBuffer ip = stack.callocInt(1);
            glGetProgramiv(ID, GL_LINK_STATUS, ip);
            int success = ip.get(0);
            if (success != GL_TRUE) {
                String info = glGetProgramInfoLog(ID, 1024);
                System.err.println("Program " + name + " linkage resulted in an error:");
                System.err.println(info);
                glDeleteProgram(ID);
                return;
            }
        }

        for (Integer sID : sIDS) {
            glDetachShader(ID, sID);
            glDeleteShader(sID);
        }

        buildUniforms();
        buildAttributes();
    }

    /**
     * Use the program
     */
    public void bind() {
        if (GameEngine.shaderManager.curProgram == ID) return;
        glUseProgram(ID);
        GameEngine.shaderManager.curProgram = ID;
    }

    /**
     * Load and compile shader
     *
     * @param type type of shader
     * @param path
     * @return empty if any failure occurred, oth. the id of the shader object
     */
    private Optional<Integer> loadShader(int type, Path path) {
        String content = null;
        try {
            content = Files.readString(path);
        } catch (Exception i) {
            //System.err.println("Could not read shader from " + path.toAbsolutePath());
            return Optional.empty();
        }

        int shad = glCreateShader(type);
        glShaderSource(shad, content);
        glCompileShader(shad);

        // Check if compilation succeeded
        try (MemoryStack stack = stackPush()) {
            IntBuffer ip = stack.callocInt(1);
            glGetShaderiv(shad, GL_COMPILE_STATUS, ip);
            int success = ip.get(0);
            if (success != GL_TRUE) {
                String info = glGetShaderInfoLog(shad, 1024);
                System.err.println("Shader " + path.toAbsolutePath() + " compilation resulted in an error:");
                System.err.println(info);
                glDeleteShader(shad);
                return Optional.empty();
            }
        }
        return Optional.of(shad);
    }

    /**
     * Get all active uniforms and store them in uniforms map
     */
    private void buildUniforms() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer ip = stack.callocInt(1);
            glGetProgramiv(ID, GL_ACTIVE_UNIFORMS, ip);
            int uniformCount = ip.get(0);

            IntBuffer info = stack.callocInt(2);
            for (int i = 0; i < uniformCount; i++) {
                String name = glGetActiveUniform(ID, i, info.position(0), info.position(1));
                int size = info.get(0);
                int type = info.get(1);

                int loc = glGetUniformLocation(ID, name);
                uniforms.put(name, loc);
            }
        }
    }

    /**
     * Get all active attributes and store them in attribute map
     */
    private void buildAttributes() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer ip = stack.callocInt(1);
            glGetProgramiv(ID, GL_ACTIVE_ATTRIBUTES, ip);
            int uniformCount = ip.get(0);

            IntBuffer d = stack.callocInt(3);
            for (int i = 0; i < uniformCount; i++) {

                String name = glGetActiveAttrib(ID, i, d.position(0), d.position(1));

                // Could use these for error checking when passing uniforms
                int size = d.get(0);
                int type = d.get(1);

                int loc = glGetAttribLocation(ID, name);
                attributes.put(name, loc);
            }
        }
    }
}
