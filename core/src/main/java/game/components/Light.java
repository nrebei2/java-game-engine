package game.components;

import util.Vector3;

public abstract class Light {
    public enum Type {
        POINT, DIRECTIONAL, SPOTLIGHT
    }
    public Vector3 color = new Vector3(1, 1, 1);
    public float intensity = 1;
    public abstract Type getType();
}
