package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages (caches) ShaderPrograms
 */
public class ShaderManager {
    private static Map<String, ShaderProgram> programs = new HashMap<>();

    /**
     * See {@link ShaderProgram#ShaderProgram(String)}
     */
    public static ShaderProgram getProgram(String name) {
        ShaderProgram prog = programs.get(name);
        if (prog != null) return prog;

        ShaderProgram program = new ShaderProgram(name);
        programs.put(name, program);
        return program;
    }
}
