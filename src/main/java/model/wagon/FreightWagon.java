package model.wagon;

import model.Wagon;

/**
 * @author Finn Bon
 */
public class FreightWagon extends Wagon {

	private final int maxWeight;

	public FreightWagon(int wagonId, int maxWeight) {
		super(wagonId);
		this.maxWeight = maxWeight;
	}
}
