import model.*;

import model.wagon.FreightWagon;
import model.wagon.PassengerWagon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainsTest {

    private List<PassengerWagon> pwList;
    private List<FreightWagon> fwList;
    private Train firstPassengerTrain;
    private Train secondPassengerTrain;
    private Train firstFreightTrain;
    private Train secondFreightTrain;

    @BeforeEach
    private void makeListOfPassengerWagons() {
        pwList = IntStream
            .rangeClosed(1, 6)
            .mapToObj(
                id -> new PassengerWagon(
                    id,
                    100
                )
            )
            .collect(Collectors.toList());
    }

    @BeforeEach
    public void makeListOfFreightWagons() {
        fwList = IntStream
            .rangeClosed(1, 6)
            .mapToObj(
                id -> new FreightWagon(
                    id,
                    100
                )
            )
            .collect(Collectors.toList());
    }

    private void makeTrains() {
        Locomotive thomas = new Locomotive(2453, 7);
        Locomotive gordon = new Locomotive(5277, 8);
        Locomotive emily = new Locomotive(4383, 11);
        Locomotive rebecca = new Locomotive(2275, 16);

        firstPassengerTrain = new Train(thomas, "Amsterdam", "Haarlem");
        for (Wagon w : pwList) {
            Shunter.hookWagonOnTrainRear(firstPassengerTrain, w);
        }
        secondPassengerTrain = new Train(gordon, "Joburg", "Cape Town");

        firstFreightTrain = new Train(emily, "Utrecht", "Rotterdam");
        for (FreightWagon w : fwList) {
            Shunter.hookWagonOnTrainRear(firstFreightTrain, w);
        }
        secondFreightTrain = new Train(rebecca, "Haarlem", "Schiphol");
    }

    @Test
    public void checkNumberOfWagonsOnTrain() {
        makeTrains();
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
    }

    @Test
    public void checkNumberOfSeatsOnTrain() {
        makeTrains();
        assertEquals( pwList.size() * 100, firstPassengerTrain.getNumberOfSeats());
    }

    @Test
    public void checkWeightOnTrain() {
        makeTrains();
        assertEquals(fwList.size() * 100, firstFreightTrain.getTotalMaxWeight());
    }

    @Test
    public void checkPositionOfWagons() {
        makeTrains();
        int position = 1;
        for (PassengerWagon pw : pwList) {
            assertEquals( position, firstPassengerTrain.getPositionOfWagon(pw.getWagonId()));
            position++;
        }
    }

    @Test
    public void checkHookOneWagonOnTrainFront() {
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(21, 140));
        assertEquals( 7, firstPassengerTrain.getNumberOfWagons(), "Train should have 7 wagons");
        assertEquals( 1, firstPassengerTrain.getPositionOfWagon(21));
    }

    @Test
    public void checkHookRowWagonsOnTrainRearFalse() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, w1);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have still have 6 wagons, capacity reached");
        assertEquals( -1, firstPassengerTrain.getPositionOfWagon(43));
    }

    @Test
    public void checkMoveOneWagon() {
        makeTrains();
        int id = 3;
        Shunter.moveOneWagon(firstPassengerTrain, secondPassengerTrain, pwList.get(id));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons(), "Train should have 5 wagons");
        assertEquals( -1, firstPassengerTrain.getPositionOfWagon(999));
        assertEquals(1, secondPassengerTrain.getNumberOfWagons(), "Train should have 1 wagon");
        assertEquals( 1, secondPassengerTrain.getPositionOfWagon(id + 1));
    }

    @Test
    public void checkMoveRowOfWagons() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(secondPassengerTrain, w1);
        Shunter.moveAllFromTrain(firstPassengerTrain, secondPassengerTrain, pwList.get(2));
        assertEquals(2, firstPassengerTrain.getNumberOfWagons(), "Train should have 2 wagons");
        assertEquals( 2, firstPassengerTrain.getPositionOfWagon(2));
        assertEquals(6, secondPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        assertEquals( 4, secondPassengerTrain.getPositionOfWagon(4));
    }

    @Test
    public void checkSuitableWagon(){
        makeTrains();
        Wagon w1 = new FreightWagon(53, 140);
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, w1);
        Shunter.hookWagonOnTrainRear(secondFreightTrain, w1);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        Shunter.moveAllFromTrain(firstPassengerTrain, secondFreightTrain, pwList.get(2));
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        assertEquals(1, secondFreightTrain.getNumberOfWagons(), "Train should have 1 wagons");
    }

    @Test
    public void checkTrainIterator() {
        makeTrains();
        int count = 1;
        for (Wagon wagon : firstPassengerTrain) {
            assertEquals(count++, wagon.getWagonId());
        }
    }

}
