package game.components;

import util.Matrix4;
import util.Vector3;

/**
 * Component holding the transform of the entity.
 */
public class Transform {
    private Vector3 position;
    // euler angles
    private Vector3 rotation;
    private Vector3 scale;

    private Matrix4 modelCache;
    boolean dirty;

    public Transform(Vector3 position, Vector3 rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        modelCache = new Matrix4();
        dirty = true;
    }

    /**
     * Set after getting to update the model matrix
     */
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.dirty = true;
        this.position = position;
    }

    /**
     * Set after getting to update the model matrix
     */
    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.dirty = true;
        this.rotation = rotation;
    }

    /**
     * Set after getting to update the model matrix
     */
    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.dirty = true;
        this.scale = scale;
    }

    /**
     * @return The model matrix for this transform (Translation * Rotation * Scale)
     */
    public Matrix4 getModel() {
        if (dirty) {
            modelCache.idt();
            modelCache.trn(position);
            modelCache.mul(Matrix4.rotate_xyz(rotation.x, rotation.y, rotation.z));
            modelCache.mul(new Matrix4().scale(scale.x, scale.y, scale.z));
            // modelCache = T * R * S
            dirty = false;
        }
        // else unchanged
        return modelCache;
    }
}
