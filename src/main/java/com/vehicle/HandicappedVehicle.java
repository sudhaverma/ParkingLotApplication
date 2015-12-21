package main.java.com.vehicle;

import main.java.com.vehicle.Vehicle.VehicleType;

public class HandicappedVehicle extends Vehicle {
	
	public HandicappedVehicle(String licensePlate) {
		super(licensePlate);
		this.vType = VehicleType.Handicapped;
		this.licensePlate = licensePlate;
	}

}
