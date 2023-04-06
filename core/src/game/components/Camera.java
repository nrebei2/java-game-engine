package game.components;

import util.Matrix4;
import util.Vector3;

import java.util.Arrays;

/**
 * Camera component
 */
public class Camera {
    private Matrix4 proj;

    /** Transform of this camera */
    private Transform transform;

    /**
     * @param pos Initial position of camera
     * @param euler_angles A rotation of (0,0,0) makes the camera look along +x with its up vector at +z.
     * @param near distance from near plane
     * @param far distance from far plane
     * @param fovy The field of view of the height in radians
     * @param asp the "width over height" aspect ratio
     */
    public Camera(Vector3 pos, Vector3 euler_angles, float near, float far, float fovy, float asp) {
        this.transform = new Transform(pos, euler_angles, new Vector3(1, 1, 1));
        proj = Matrix4.projection(near, far, fovy, asp);
    }

    public Matrix4 getViewProj() {
        // view = (R^-1)(T^-1) = (R^T)(T^-1)
        Matrix4 view = transform.getModel().cpy().inv();
        // first view, then proj
        return view.mulLeft(proj);
    }

    public Transform getTransform() {
        return transform;
    }
}
