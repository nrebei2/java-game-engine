package game.components;

import util.Matrix4;
import util.Vector3;

public class Spotlight extends Light {
    // To retrieve projection for shadow
    private Camera camera;
    public float range;

    public Spotlight(Vector3 position, Vector3 euler_angles, float spot_angle, float range) {
        camera = new Camera(position, euler_angles, 0.01f, 10000, spot_angle / 2, 1);
        this.range = range;
    }

    public Transform getTransform() {
        return camera.getTransform();
    }

    public Matrix4 getViewProj() {
        return camera.getViewProj();
    }

    public Type getType() {
        return Type.SPOTLIGHT;
    };
}
