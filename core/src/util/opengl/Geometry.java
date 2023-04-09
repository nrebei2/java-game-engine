package util.opengl;

import util.UnorderedList;
import util.ecs.Identifiable;

/**
 * Container of (named) vertex attributes.
 * Note when attached to a mesh, the mesh will try to match the names of the attributes with the material's shader attributes.
 */
public class Geometry {
    /**
     * Map from names (case-sensitive) to attributes
     */
    public UnorderedList<AttrInfo> attributes = new UnorderedList<>(3);

    public int[] indices;

    private int count = -1;

    /**
     * Are all attributes dynamic
     */
    boolean dynamic = false;

    /**
     * Offset for next added attribute
     * Conveniently also the total size in bytes of all attributes
     */
    int offset = 0;

    boolean dirty = true;

    /**
     * Attaches an attribute to this geometry. There is no protection for assigning the same attribute to multiple geometries.
     *
     * @param name      attribute name in shader
     * @param attribute
     * @return same geometry object for chaining
     */
    public Geometry addAttribute(String name, VertexAttribute attribute) {
        if (count == -1) {
            count = attribute.count;
        } else {
            if (attribute.count != count)
                throw new RuntimeException("Attribute count mismatch! All attributes attached to this geometry should have the same count.");
        }
        dirty = true;
        dynamic = dynamic && attribute.dynamic;

        AttrInfo info = new AttrInfo();
        info.attribute = attribute;
        info.name = name;
        info.offset = offset;
        // update offset for next attribute
        offset += attribute.sizeof;
        attributes.add(info);
        return this;
    }

    /**
     * Will update the dirty bit of the attribute to become true. Why else are you getting it?
     *
     * @param name case-sensitive name of attribute
     * @return The attribute if attached, null oth.
     */
    public VertexAttribute getAttribute(String name) {
        for (AttrInfo info : attributes) {
            if (info.name.equals(name)) {
                info.dirty = true;
                return info.attribute;
            }
        }
        return null;
    }


    /**
     * @param indices null to remove indices
     * @return same geometry object for chaining
     */
    public Geometry setIndices(int[] indices) {
        this.indices = indices;
        return this;
    }

    /**
     * @return count of distinct vertices in an attribute
     */
    public int count() {
        return count;
    }

    class AttrInfo extends Identifiable {
        VertexAttribute attribute;
        /**
         * name in shader
         */
        String name;
        /**
         * Byte position of attribute data
         */
        int offset;
        /**
         * Whether the underlying data has been changed since the last render call
         */
        boolean dirty;
    }
}
