package util.ecs;

public interface System {

    /**
     * Logic this system will do.
     *
     * @param engine Engine this system is attached to
     */
    void run(Engine engine);
}
