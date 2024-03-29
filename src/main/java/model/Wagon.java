package model;

// uitleg
public class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon (int wagonId) {
        this.wagonId = wagonId;
    }

    // uitleg
    public Wagon getLastWagonAttached() {
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon
        return nextWagon == null ? this : nextWagon.getLastWagonAttached();
    }

    public void setNextWagon(Wagon nextWagon) {
        // when setting the next wagon, set this wagon to be previous wagon of next wagon
        this.nextWagon = nextWagon;
        if (nextWagon != null)
            nextWagon.setPreviousWagon(this);
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }

    // uitleg
    public int getNumberOfWagonsAttached() {
        // Recursively call number of wagons behind from next wagons
        // Wagon without nextWagon returns 0
        return this.hasNextWagon() ? this.nextWagon.getNumberOfWagonsAttached() + 1 : 0;
    }

    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
