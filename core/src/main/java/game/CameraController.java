package game;

import game.components.Camera;
import util.GameEngine;
import util.Matrix4;
import util.Vector3;
import util.ecs.Engine;
import util.ecs.System;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Free-cam controller
 */
public class CameraController implements System {
    /**
     * Camera this class is controlling
     */
    Camera camera;
    private float movementSpeed;
    private float shiftScale;
    private float mouseSensivity;

    /**
     * Cache
     */
    private float[] forward, right;

    /**
     * @param camera           this class will control
     * @param movementSpeed    units/second
     * @param shiftScale       speed scale increase when holding shift
     * @param mouseSensitivity degrees/pixel
     */
    public CameraController(Camera camera, float movementSpeed, float shiftScale, float mouseSensitivity) {
        this.camera = camera;
        this.movementSpeed = movementSpeed;
        this.shiftScale = shiftScale;
        this.mouseSensivity = mouseSensitivity;
        forward = new float[3];
        right = new float[3];
    }

    /**
     * Update the transform of this camera given device input.
     */
    public void run(Engine engine, float deltaTime) {
        // Movement
        float delta = movementSpeed * deltaTime;
        if (GameEngine.input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) delta *= shiftScale;
        if (GameEngine.input.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) delta /= shiftScale;

        Vector3 angles = camera.getTransform().getRotation();
        float[] rotMat = Matrix4.rotate_xyz(angles.x, angles.y, angles.z);

        // Forward vector
        forward[0] = 1;
        forward[1] = 0;
        forward[2] = 0;
        Matrix4.mulVec(rotMat, forward);

        // Right vector
        right[0] = 0;
        right[1] = -1;
        right[2] = 0;
        Matrix4.mulVec(rotMat, right);

        Vector3 position = camera.getTransform().getPosition();
        if (GameEngine.input.isKeyPressed(GLFW_KEY_W)) {
            position.add(forward[0] * delta, forward[1] * delta, forward[2] * delta);
        }
        if (GameEngine.input.isKeyPressed(GLFW_KEY_S)) {
            position.sub(forward[0] * delta, forward[1] * delta, forward[2] * delta);
        }
        if (GameEngine.input.isKeyPressed(GLFW_KEY_D)) {
            position.add(right[0] * delta, right[1] * delta, right[2] * delta);
        }
        if (GameEngine.input.isKeyPressed(GLFW_KEY_A)) {
            position.sub(right[0] * delta, right[1] * delta, right[2] * delta);
        }
        camera.getTransform().setPosition(position);

        // Rotation
        if (Math.abs(GameEngine.input.getDeltaY()) >= 600 || Math.abs(GameEngine.input.getDeltaX()) >= 600) return;
        float limit = (float) (Math.PI / 2.0);
        angles.y = clamp(angles.y - mouseSensivity * GameEngine.input.getDeltaY() * (float) Math.PI / 180, -limit, limit);
        angles.z -= mouseSensivity * GameEngine.input.getDeltaX() * (float) Math.PI / 180;
        camera.getTransform().setRotation(angles);

        // Zoom-in
        camera.setFovy(clamp(camera.getFovy() - (GameEngine.input.getScrollY() * (float) Math.PI / 100), 0, (float) Math.PI));
    }

    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
