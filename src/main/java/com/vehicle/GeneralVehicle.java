package main.java.com.vehicle;

import main.java.com.vehicle.Vehicle.VehicleType;

public class GeneralVehicle extends Vehicle {
	
	
	public GeneralVehicle(String licensePlate) {
	    super(licensePlate);
		this.vType = VehicleType.General;
		this.licensePlate = licensePlate;
	}
	
	
	

}
