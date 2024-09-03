package util.ecs;

public abstract class Identifiable {
    /**
     * integer identifier assigned to this object
     */
    int identifier;

    public void setId(int id) {
        this.identifier = id;
    }

    public int getId() {
        return identifier;
    }
}
