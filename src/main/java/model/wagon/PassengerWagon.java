package model.wagon;

import model.Wagon;

/**
 * @author Finn Bon
 */
public class PassengerWagon extends Wagon {

	private final int numberOfSeats;

	public PassengerWagon(int wagonId, int numberOfSeats) {
		super(wagonId);
		this.numberOfSeats = numberOfSeats;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}
}
