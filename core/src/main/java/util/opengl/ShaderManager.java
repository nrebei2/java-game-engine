package util.opengl;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages (caches) ShaderPrograms.
 */
public class ShaderManager {
    // Shader name to program object
    private final Map<String, ShaderProgram> programs = new HashMap<>();

    /**
     * Current bound program ID
     */
    public int curProgram = -1;

    /**
     * Calls {@link ShaderProgram#ShaderProgram(String)} if program does not exist.
     * Retrieves a program.
     *
     * @param path Project relative path of shader location.
     * @param name name of shader.
     */
    public ShaderProgram getProgram(String path, String name) {
        ShaderProgram prog = programs.get(path + "/" + name);
        if (prog != null) return prog;

        ShaderProgram program = new ShaderProgram(path + "/" + name);
        programs.put(name, program);
        return program;
    }

    /**
     * See {@link ShaderProgram#ShaderProgram(String)}
     */
    public ShaderProgram getProgram(String name) {
        return getProgram("assets/shaders", name);
    }

    /**
     * Refresh all managed shaders
     */
    public void refresh() {
        for (var s : programs.values()) {
            s.reload();
        }
    }
}
