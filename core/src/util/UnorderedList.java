package util;

import util.ecs.Identifiable;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    Object[] elements;
    private int size;

    /** Iterator cache */
    private Itr itr = new Itr();

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
        // always just append
        elements[size()] = element;
        element.setId(index);
        size++;
    }

    public T remove(int index) {
        Object[] es = this.elements;
        T tmp = get(index);
        if (index == size() - 1) {
            // index at the end
            es[index] = null;
        } else {
            // We swap with the end
            es[index] = es[size()-1];
            es[size() - 1] = null;
        }
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

    @Override
    public Stream<T> stream() {
        Stream<T> stream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iterator(),
                        Spliterator.ORDERED)
                , false);

        return super.stream();
    }

    /**
     * Note this returns a reference of a pre-allocated instance. Therefore, do not nest iterators!
     */
    public Iterator<T> iterator() {
        itr.reset();
        return itr;
    }

    private class Itr implements Iterator<T> {
        int curIndex; // Index of next element

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return curIndex < size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public T next() {
            return get(curIndex++);
        }

        @Override
        public void remove() {
            UnorderedList.this.remove(curIndex-1);
        }

        void reset() {
            curIndex = 0;
        }
    }
}
