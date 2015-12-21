package main.java.com.parkinglot;

import main.java.com.vehicle.Vehicle;

public interface IParkingLot {
	
	/* 
	 * Given a vehicle, park it in the parking lot. if there are no entries left, the car will be expected 
	 * to be waiting for the next available slot.
	 */
	public void park(Vehicle v) throws InterruptedException;
	public void unPark(Vehicle v) throws InterruptedException;
	
	
}
