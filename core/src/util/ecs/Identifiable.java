package util.ecs;

public abstract class Identifiable {
    /**
     * integer identifier assigned to this object
     */
    int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
