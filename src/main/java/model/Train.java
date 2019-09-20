package model;

import model.wagon.FreightWagon;
import model.wagon.PassengerWagon;

import java.util.Iterator;

// uitleg
public class Train implements Iterable<Wagon> {
    private Locomotive engine;
    // uitleg
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }

    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */

       numberOfWagons = 0;
       if(!hasNoWagons()){
           numberOfWagons = firstWagon.getNumberOfWagonsAttached() + 1;
       }
    }

    public int getNumberOfWagons() {
        return numberOfWagons;
    }


    /* three helper methods that are usefull in other methods */

    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    // uitleg
    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1

        Wagon currentWagon = this.firstWagon;
        int pos = 1;

        while(currentWagon != null){
            if (currentWagon.getWagonId() == wagonId){
                return pos;
            }
            pos++;
            currentWagon = currentWagon.getNextWagon();
        }

        return -1;
    }

    // uitleg
    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        if (position < 1 || position > numberOfWagons) {
            throw new IndexOutOfBoundsException("This train does not have a wagon on the specified position position");
        }

        Wagon lastCheckedWagon = this.firstWagon;

        for (int i = 1; i != position; i++) {
            lastCheckedWagon = lastCheckedWagon.getNextWagon();
        }

        return lastCheckedWagon;
    }

    // uitleg
    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */

        if (isFreightTrain())
            return 0;

        int numberOfSeats = 0;

        for (Wagon wagon : this) {
            if (wagon instanceof PassengerWagon) {
                numberOfSeats += ((PassengerWagon) wagon).getNumberOfSeats();
            }
        }

        return numberOfSeats;
    }

    // uitleg
    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */
        if (isPassengerTrain())
            return 0;

        int totalWeight = 0;

        for (Wagon wagon : this) {
            if (wagon instanceof FreightWagon) {
                totalWeight += ((FreightWagon) wagon).getMaxWeight();
            }
        }

        return totalWeight;
    }

    public Locomotive getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        for (Wagon wagon : this) {
            result.append(wagon.toString());
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator iterator() {
        return new TrainWagonIterator(this);
    }
}

// uitleg
class TrainWagonIterator implements Iterator<Wagon> {

    private Train train;
    private int pos;

    public TrainWagonIterator(Train train) {
        this.train = train;
        pos = 0;
    }

    @Override
    public boolean hasNext() {
        return pos < train.getNumberOfWagons();
    }

    @Override
    public Wagon next() {
        return train.getWagonOnPosition(++pos);
    }
}
