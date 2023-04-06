package util.ecs;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity part of the ECS. No state other than the components it holds.
 */
public final class Entity extends Identifiable {
    /** cache[c] = comp iff component comp is attached to this entity with class c */
    Map<Class<?>, Object> cache;

    /** Whether systems will be able to */
    boolean enabled;

    public Entity() {
        cache = new HashMap<>();
        enabled = true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Adds a component to this entity. There is no protection for assigning the same component to multiple entities.
     * @param component to add
     * @return same entity for chaining
     * @throws RuntimeException if you assign a component of an already attached class
     */
    public Entity add(Object component) {
        if (cache.containsKey(component.getClass())) {
            throw new RuntimeException("Entity can only have at most one component of a given class!");
        }
        cache.put(component.getClass(), component);
        return this;
    }

    /**
     * @param component class of component
     * @return null if there is no component, the actual component otherwise
     */
    public <T> T getComponent(Class<T> component) {
        return (T)cache.get(component);
    }

    /**
     * Removes a component if present
     */
    public Object remove(Object component) {
        return cache.remove(component.getClass());
    }

    /**
     * Removes a component if there is a component of the specified type.
     * @return the component that was removed, null oth.
     */
    public Object removeType(Class<?> component) {
        return cache.remove(component);
    }

    /**
     * @return Whether the specified component is present (==).
     */
    public boolean contains(Object component) {
        return cache.get(component.getClass()) == component;
    }

    /**
     * @return Whether there is a component of the specified class.
     */
    public boolean containsType(Class<?> component) {
        return cache.containsKey(component);
    }

    public void dispose() {
        cache = null;
    }

}
