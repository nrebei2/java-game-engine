package util.opengl;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages (caches) ShaderPrograms. Singleton.
 */
public class ShaderManager {
    // Shader name to program object
    private Map<String, ShaderProgram> programs = new HashMap<>();

    /**
     * Current bound program ID
     */
    public int curProgram = -1;

    /**
     * See {@link ShaderProgram#ShaderProgram(String)}
     */
    public ShaderProgram getProgram(String name) {
        ShaderProgram prog = programs.get(name);
        if (prog != null) return prog;

        ShaderProgram program = new ShaderProgram(name);
        programs.put(name, program);
        return program;
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
