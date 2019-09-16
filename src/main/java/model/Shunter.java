package model;

import model.wagon.FreightWagon;
import model.wagon.PassengerWagon;

public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons

        if (train.hasNoWagons())
            return true;
        if (wagon instanceof PassengerWagon && train.isPassengerTrain())
            return true;
        if(wagon instanceof FreightWagon && train.isFreightTrain())
            return true;

        return false;
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons

        if (one.getClass() == two.getClass())
            return true;

        return false;
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons

        int wagonsTotal = train.getNumberOfWagons() + wagon.getNumberOfWagonsAttached() + 1;

        if (wagonsTotal <= train.getEngine().getMaxWagons())
            return true;

        return false;
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon

        return train.getNumberOfWagons() +1 <= train.getEngine().getMaxWagons();
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */

        if (hasPlaceForWagons(train, wagon) && isSuitableWagon(train, wagon)){
            if (train.hasNoWagons()) {
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            }

            if (hookWagonOnWagon(train.getFirstWagon().getLastWagonAttached(), wagon)){
                train.resetNumberOfWagons();
                return true;
            }
        }

         return false;
    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */

        if(hasPlaceForWagons(train, wagon) && isSuitableWagon(train, wagon)){
            if (train.hasNoWagons()){
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            }
            if (hookWagonOnWagon(wagon.getLastWagonAttached(), train.getFirstWagon())){
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            }
        }

        return false;
    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
        * if so make second wagon next wagon of first */

        if (isSuitableWagon(first, second)){
            first.setNextWagon(second);
            return true;
        }

        return false;
    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */

        if (train.getPositionOfWagon(wagon.getWagonId()) > -1){
            wagon.getPreviousWagon().setNextWagon(null);
            wagon.setPreviousWagon(null);
            train.resetNumberOfWagons();
            return true;
        }

        return false;
    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */

        if (train.getPositionOfWagon(wagon.getWagonId()) > -1){
            if (wagon.hasPreviousWagon()) {
                wagon.getPreviousWagon().setNextWagon(wagon.getNextWagon());
                wagon.setNextWagon(null);
                wagon.setPreviousWagon(null);
                train.resetNumberOfWagons();
                return true;
            }
            else{
                wagon.getNextWagon().setPreviousWagon(null);
                train.setFirstWagon(wagon.getNextWagon());
                wagon.setNextWagon(null);
                train.resetNumberOfWagons();
                return true;
            }
        }

         return false;
    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */

        if (from.getPositionOfWagon(wagon.getWagonId()) > -1){
            if (isSuitableWagon(to, wagon) && hasPlaceForWagons(to, wagon)){
                if  (detachAllFromTrain(from, wagon))
                    return hookWagonOnTrainRear(to, wagon);
            }
        }

        return false;
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon

        if (from.getPositionOfWagon(wagon.getWagonId()) > -1){
            if (isSuitableWagon(to, wagon) && hasPlaceForOneWagon(to, wagon)){
                if (detachOneWagon(from, wagon)){
                    return hookWagonOnTrainRear(to, wagon);
                }
            }
        }

        return false;
    }
}
