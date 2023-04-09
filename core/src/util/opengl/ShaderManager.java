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
     * Singleton instance
     */
    private static ShaderManager instance;

    protected ShaderManager() {
    }

    /**
     * @return The singleton instance of this class
     */
    public static ShaderManager getInstance() {
        if (instance == null) {
            instance = new ShaderManager();
        }
        return instance;
    }

    /**
     * See {@link ShaderProgram#ShaderProgram(String)}
     */
    public ShaderProgram getProgram(String name) {
        ShaderProgram prog = programs.get(name);
        if (prog != null) return prog;

        ShaderProgram program = new ShaderProgram(name);
        program.manager = this;
        programs.put(name, program);
        return program;
    }
}
