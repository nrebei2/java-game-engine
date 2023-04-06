package game;

import game.components.Camera;
import util.GameEngine;
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
     * @param camera this class will control
     */
    public CameraController(Camera camera, float movementSpeed, float shiftScale, float mouseSensitivity) {
        this.camera = camera;
        this.movementSpeed = movementSpeed;
        this.shiftScale = shiftScale;
        this.mouseSensivity = mouseSensitivity;
    }

    /**
     * Update the transform of this camera given device input.
     */
    public void run(Engine engine, float deltaTime) {
        // Movement
        float delta = movementSpeed * deltaTime;
        if (GameEngine.input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) delta *= shiftScale;

        Vector3 angles = camera.getTransform().getRotation();
        float sY = (float) Math.sin(angles.y);
        float cY = (float) Math.cos(angles.y);
        float sZ = (float) Math.sin(angles.z);
        float cZ = (float) Math.cos(angles.z);

        // Forward vector
        float f_x = cY * cZ;
        float f_y = cY * sZ;
        float f_z = sY;

        // Right vector
        float r_x = sZ;
        float r_y = -cZ;
        float r_z = 0;

        Vector3 position = camera.getTransform().getPosition();
        if (GameEngine.input.isKeyPressed(GLFW_KEY_W)) {
            position.add(f_x * delta, f_y * delta, f_z * delta);
        }
        if (GameEngine.input.isKeyPressed(GLFW_KEY_S)) {
            position.sub(f_x * delta, f_y * delta, f_z * delta);
        }
        if (GameEngine.input.isKeyPressed(GLFW_KEY_D)) {
            position.add(r_x * delta, r_y * delta, r_z * delta);
        }
        if (GameEngine.input.isKeyPressed(GLFW_KEY_A)) {
            position.sub(r_x * delta, r_y * delta, r_z * delta);
        }
        camera.getTransform().setPosition(position);

        // Rotation
        float limit = (float) (Math.PI / 2.0);
        angles.y = clamp(angles.y - mouseSensivity * GameEngine.input.getDeltaY(), -limit, limit);
        angles.z -= mouseSensivity * GameEngine.input.getDeltaX();
        camera.getTransform().setRotation(angles);

        //java.lang.System.out.println(camera.getTransform().getPosition());
        //java.lang.System.out.println(camera.getTransform().getRotation());
    }

    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
