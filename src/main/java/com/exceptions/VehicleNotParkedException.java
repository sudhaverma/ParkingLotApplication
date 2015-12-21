package main.java.com.exceptions;

/**
 * This is custom exception class created to handle the scenarios when the user tries to unpark the vehicle which is not parked in Parking lot
 * @author Sudha
 *
 */
public class VehicleNotParkedException extends Exception {
	
	    public VehicleNotParkedException(String msg) {
	        super(msg);
	    }

		public VehicleNotParkedException() {
			// TODO Auto-generated constructor stub
		}
}

