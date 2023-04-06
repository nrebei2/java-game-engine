package util.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Independent container for all ECS data
 */
public class Engine {
    /** Entities attached to this engine */
    UnorderedList<Entity> entities;

    Result<?> resultCache;

    /** Systems attached to this engine */
    List<System> systems;

    public Engine() {
        entities = new UnorderedList<>();
        systems = new ArrayList<>();
        resultCache = new Result<>();
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
     * Disable the entity from all systems attached to this engine.
     */
    public boolean disableEntity(Entity entity) {
        return setEntity(entity, false);
    }

    /**
     * Enable the entity for all systems attached to this engine.
     */
    public boolean enableEntity(Entity entity) {
        return setEntity(entity, true);
    }

    private boolean setEntity(Entity entity, boolean enabled) {
        if (entities.contains(entity)) {
            entity.setEnabled(enabled);
            return true;
        }
        return false;
    }

    /**
     * Finds all entities with components of the specified type.
     * @return a stream of components (of no particular order)
     */
    public <T> Stream<Result<T>> findEntitiesWith(Class<T> component) {
        resultCache.components = null;
        AtomicInteger index = new AtomicInteger();
        return entities.stream().filter(e -> e.containsType(component) && e.isEnabled()).
                map((e) -> {
                    int idx = index.getAndIncrement();
                    var res = (Result<T>) resultCache;
                    res.entity = e;
                    res.components = e.getComponent(component);
                    return res;
                });
    }

    /**
     * Finds all entities with components of the specified types.
     * @return a stream of components (of no particular order)
     */
    public <T1, T2> Stream<Result<With2<T1, T2>>> findEntitiesWith(Class<T1> comp1, Class<T2> comp2) {
        resultCache.components = null;
        return entities.stream().filter(e -> e.containsType(comp1) && e.containsType(comp2)).
                map((e) -> {
                    var res = (Result<With2<T1,T2>>) resultCache;
                    res.entity = e;
                    res.components.comp1 = e.getComponent(comp1);
                    res.components.comp2 = e.getComponent(comp2);
                    return res;
                });
    }

    /**
     * Finds all entities with components of the specified types.
     * @return a stream of components (of no particular order)
     */
    public <T1, T2, T3> Stream<Result<With3<T1, T2, T3>>> findEntitiesWith(Class<T1> comp1, Class<T2> comp2, Class<T3> comp3) {
        resultCache.components = null;
        return entities.stream().filter(e -> e.containsType(comp1) && e.containsType(comp2) && e.containsType(comp3)).
                map((e) -> {
                    var res = (Result<With3<T1,T2,T3>>) resultCache;
                    res.entity = e;
                    res.components.comp1 = e.getComponent(comp1);
                    res.components.comp2 = e.getComponent(comp2);
                    res.components.comp3 = e.getComponent(comp3);
                    return res;
                });
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

    /**
     * Container holding an entity that match a set of components
     */
    public class Result<T> {
        /**
         * Entity holding set of components
         */
        public Entity entity;

        /**
         * Container holding set of components
         */
        public T components;
    }
    public class With2<T1, T2> {
        public T1 comp1;
        public T2 comp2;
    }

    public class With3<T1, T2, T3> {
        public T1 comp1;
        public T2 comp2;
        public T3 comp3;
    }
}