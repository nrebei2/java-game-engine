package game.components;

import util.Matrix4;
import util.Vector3;

/**
 * Camera component
 */
public class Camera {
    /**
     * Projection matrix
     */
    private Matrix4 proj;

    /**
     * Transform of this camera
     */
    private Transform transform;

    /**
     * Cache of view matrix
     */
    private Matrix4 viewCache;
    /**
     * Distance to near plane
     */
    private float near;
    /**
     * Distance to far plane
     */
    private float far;
    /**
     * The field of view of the height in radians
     */
    private float fovy;
    /**
     * the "width over height" aspect ratio
     */
    private float asp;

    /**
     * @param pos          Initial position of camera
     * @param euler_angles A rotation of (0,0,0) makes the camera look along +x with its up vector at +z.
     *                     As we assume a right-handed coordinate system, right points along is -y.
     * @param near         distance from near plane
     * @param far          distance from far plane
     * @param fovy         The field of view of the height in radians
     * @param asp          the "width over height" aspect ratio
     */
    public Camera(Vector3 pos, Vector3 euler_angles, float near, float far, float fovy, float asp) {
        this.transform = new Transform(pos, euler_angles, new Vector3(1, 1, 1));
        this.near = near;
        this.far = far;
        this.fovy = fovy;
        this.asp = asp;
        proj = Matrix4.projection(near, far, fovy, asp);
        viewCache = new Matrix4();
    }

    /**
     * @return View-Projection matrix of this camera
     */
    public Matrix4 getViewProj() {
        // view = (R^-1)(T^-1) = (R^T)(T^-1)
        Vector3 rot = transform.getRotation();
        Vector3 tr = transform.getPosition();
        viewCache.set(Matrix4.rotate_xyz(rot.x, rot.y, rot.z));
        viewCache.tra();
        viewCache.translate(-tr.x, -tr.y, -tr.z);

        // first view, then proj
        return viewCache.mulLeft(proj);
    }

    public Transform getTransform() {
        return transform;
    }

    public void setFovy(float fovy) {
        this.fovy = fovy;
        proj = Matrix4.projection(near, far, fovy, asp);
    }

    public void setAsp(float asp) {
        this.asp = asp;
        proj = Matrix4.projection(near, far, fovy, asp);
    }

    public float getFovy() {
        return fovy;
    }
}
