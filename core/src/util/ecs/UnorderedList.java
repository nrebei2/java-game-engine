package util.ecs;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

/**
 * ArrayList with no guarantees on order. This allows remove to be O(1).
 *
 * @param <T> Extends identifiable. This structure will modify its elements ids for fast access.
 *            UB if you edit an elements id while in this structure.
 */
public class UnorderedList<T extends Identifiable> extends AbstractList<T> implements List<T>, RandomAccess {
    /**
     * capacity = entities.length
     */
    private Object[] elements;
    private int size;

    public UnorderedList() {
        this(10);
    }

    public UnorderedList(int capacity) {
        elements = new Object[capacity];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Specialized method, requires == (not just Objects.equals)
     */
    public boolean contains(T o) {
        if (o.getId() < 0 || o.getId() >= size()) {
            return false;
        }
        return elements[o.getId()] == o;
    }

    public T get(int index) {
        checkBounds(index);
        return (T) elements[index];
    }

    public T set(int index, T element) {
        T tmp = get(index);
        elements[index] = element;
        element.setId(index);
        return tmp;
    }

    /**
     * Have to add this so it works nicely with AbstractList's implementations :(.
     * Note this just appends the element to the end regardless of index.
     */
    public void add(int index, T element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        if (size() == elements.length) {
            resize();
        }
        // always just append appending
        elements[size()] = element;
        element.setId(index);
        size++;
    }

    public T remove(int index) {
        Object[] es = this.elements;
        T tmp = get(index);
        if (index != size()) {
            // We swap with the end
            es[index] = es[size()];
        }
        es[index] = null;
        size--;
        return tmp;
    }

    /**
     * Specialized method, requires == (not just Objects.equals)
     */
    public boolean remove(T o) {
        if (contains(o)) {
            remove(o.getId());
            return true;
        }
        return false;
    }

    private void checkBounds(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Doubles the capacity.
     *
     * @throws OutOfMemoryError
     */
    private void resize() {
        int newCapacity;
        if (elements.length == 0) {
            newCapacity = 10;
        } else {
            newCapacity = elements.length << 1;
        }
        if (newCapacity < elements.length) {
            throw new OutOfMemoryError("Capacity overshot max value.");
        }
        elements = Arrays.copyOf(elements, newCapacity);
    }
}
