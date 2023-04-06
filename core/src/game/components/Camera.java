package game.components;

import util.Matrix4;
import util.Vector3;

/**
 * Camera component
 */
public class Camera {
    private Matrix4 proj;

    /**
     * Transform of this camera
     */
    private Transform transform;

    /**
     * @param pos
     * @param euler_angles (0,0,0) points along the +x axis
     * @param near
     * @param far
     * @param fov
     * @param asp
     */
    public Camera(Vector3 pos, Vector3 euler_angles, float near, float far, float fov, float asp) {
        this.transform = new Transform(pos, euler_angles, new Vector3(1, 1, 1));
        proj = Matrix4.projection(near, far, fov, asp);
    }

    public Matrix4 getViewProj() {
        // view = (R^-1)(T^-1) = (TR)^-1
        Matrix4 view = transform.getModel().inv();
        // first view, then proj
        return view.mulLeft(proj);
    }

    public Transform getTransform() {
        return transform;
    }
}
