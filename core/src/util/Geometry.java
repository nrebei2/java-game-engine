package util;

import java.util.HashMap;

/**
 * Container of (named) vertex attributes.
 * Note when attached to a mesh with a shader the names must match names of the shader attributes.
 */
public class Geometry {
    /**
     * Map from names (case-sensitive) to attributes
     */
    public HashMap<String, VertexAttribute> attributeMap = new HashMap<>();

    public int[] indices;

    private int count = -1;

    /**
     * Attaches an attribute to this geometry. There is no protection for assigning the same attribute to multiple geometries.
     *
     * @param name      Should match uniform name in shader
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
        attributeMap.put(name, attribute);
        return this;
    }

    /**
     * @param name case-sensitive name of attribute
     * @return The attribute if attached, null oth.
     */
    public VertexAttribute getAttribute(String name) {
        return attributeMap.get(name);
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
        // any attribute will do
        VertexAttribute attr = attributeMap.values().iterator().next();
        return attr.count;
    }
}
