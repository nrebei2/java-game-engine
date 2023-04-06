package game.components;

import util.Matrix4;
import util.Vector3;

/**
 * Camera component
 */
public class Camera {
    private Vector3 pos;
    private Matrix4 proj;
    private Vector3 euler_ang;

    /**
     * @param pos
     * @param euler_angles (0,0,0) points along the +x axis
     * @param near
     * @param far
     * @param fov
     * @param asp
     */
    public Camera(Vector3 pos, Vector3 euler_angles, float near, float far, float fov, float asp) {
        this.pos = pos;
        this.euler_ang = euler_angles;
        proj = Matrix4.projection(near, far, fov, asp);
    }

    public Matrix4 getViewProj() {
        // view = (R^-1)(T^-1) = (R^T)(T^-1)
        Matrix4 trans = new Matrix4();
        trans.trn(-pos.x, -pos.y, -pos.z);
        Matrix4 view = Matrix4.rotate_xyz(euler_ang.x, euler_ang.y, euler_ang.z).tra().mul(trans);
        // first view, then proj
        return view.mulLeft(proj);
    }
}
