package model;

import model.wagon.FreightWagon;
import model.wagon.PassengerWagon;

import java.util.Iterator;

public class Train implements Iterable<Wagon> {
    private Locomotive engine;
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

    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
//        Wagon wag = getFirstWagon();
//        do {
//            System.out.println(wag.getWagonId());
//            wag = wag.getNextWagon();
//        } while (wag != null);
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */
//        System.out.println("NOW: " + numberOfWagons + " POS: " + position);
        if (position < 1 || position > numberOfWagons) {
            throw new IndexOutOfBoundsException("This train does not have a wagon on the specified position position");
        }

        Wagon lastCheckedWagon = this.firstWagon;

        for (int i = 1; i != position; i++) {
            lastCheckedWagon = lastCheckedWagon.getNextWagon();
        }

        return lastCheckedWagon;
    }

    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */

        if (isFreightTrain())
            return 0;

        int numberOfSeats = 0;
        Wagon next = this.getFirstWagon();

        while (next != null) {
            if (next instanceof PassengerWagon) {
                PassengerWagon currentWagon = (PassengerWagon) next;
                numberOfSeats += currentWagon.getNumberOfSeats();
            }
            next = next.getNextWagon();
        }
        return numberOfSeats;
    }

    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */
        if (isPassengerTrain())
            return 0;

        int totalWeight = 0;
        Wagon next = this.getFirstWagon();

        while (next != null) {
            if (next instanceof FreightWagon) {
                FreightWagon currentWagon = (FreightWagon) next;
                totalWeight += currentWagon.getMaxWeight();
            }
            next = next.getNextWagon();
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
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator iterator() {
        return new TrainWagonIterator(this);
    }
}

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
