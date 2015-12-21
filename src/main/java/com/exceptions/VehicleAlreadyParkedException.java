package main.java.com.exceptions;

/**
 * Custom exception class called when a parked vehicle(identified by Vehicle no.) is tried to park again.
 * @author sudha
 *
 */
public class VehicleAlreadyParkedException extends Exception {

	public VehicleAlreadyParkedException(String msg)
	{
		super(msg);
	}

	public VehicleAlreadyParkedException() {
	
	}
}
