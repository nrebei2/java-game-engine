package util.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Independent container for all ECS data
 */
public class Engine {
    /** Entities attached to this engine */
    UnorderedList<Entity> entities;

    /** Systems attached to this engine */
    List<System> systems;

    public Engine() {
        entities = new UnorderedList<>();
        systems = new ArrayList<>();
    }

    /**
     * Create entity with initial components
     * @param components to add to the entity
     * @return Created entity
     */
    public Entity createEntity(Object... components) {
        Entity entity = new Entity();
        for (Object c: components) {
            entity.add(c);
        }
        entities.add(entity);
        return entity;
    }

    /**
     * Requires == for entity (not just Objects.equals)
     * @param entity to remove from system
     * @return whether entity was removed or not
     */
    public boolean deleteEntity(Entity entity) {
        if (entities.remove(entity)) {
            entity.dispose();
            return true;
        }
        return false;
    }

    /**
     * Finds all entities with components of the specified type.
     * @return a stream of components (of no particular order)
     */
    public <T> Stream<T> findEntitiesWith(Class<T> component) {
        return entities.stream().filter(e -> e.containsType(component)).map(e -> e.getComponent(component));
    }

    /**
     * Finds all entities with components of the specified types.
     * @return a stream of components (of no particular order)
     */
    public <T1, T2> Stream<With2<T1, T2>> findEntitiesWith(Class<T1> comp1, Class<T2> comp2) {
        return entities.stream().filter(e -> e.containsType(comp1) && e.containsType(comp2)).
                map(e -> new With2<>(e.getComponent(comp1), e.getComponent(comp2)));
    }

    /**
     * Finds all entities with components of the specified types.
     * @return a stream of components (of no particular order)
     */
    public <T1, T2, T3> Stream<With3<T1, T2, T3>> findEntitiesWith(Class<T1> comp1, Class<T2> comp2, Class<T3> comp3) {
        return entities.stream().filter(e -> e.containsType(comp1) && e.containsType(comp2) && e.containsType(comp3)).
                map(e -> new With3<>(e.getComponent(comp1), e.getComponent(comp2), e.getComponent(comp3)));
    }

    /**
     * @param system that will be attached to this engine
     */
    public void addSystem(System system) {
        systems.add(system);
    }

    /** Run all systems attached to this engine */
    public void run() {
        for (System s: systems) {
            s.run(this);
        }
    }

    public class With2<T1, T2> {
        public T1 comp1;
        public T2 comp2;

        public With2(T1 c1, T2 c2) {
            comp2 = c2;
            comp1 = c1;
        }
    }

    public class With3<T1, T2, T3> {
        public T1 comp1;
        public T2 comp2;
        public T3 comp3;

        public With3(T1 c1, T2 c2, T3 c3) {
            comp3 = c3;
            comp2 = c2;
            comp1 = c1;
        }
    }
}