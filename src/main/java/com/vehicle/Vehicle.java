package main.java.com.vehicle;


public abstract class Vehicle {

	
	//we are handling only general and handicapped types in this case
	public enum VehicleType {
			General,
			Handicapped
	}
	
	protected int parkOrUnpark;
	
	//identifier for a vehicle
	protected String licensePlate;
	protected VehicleType vType;
	
	public int getParkOrUnpark() {
		return parkOrUnpark;
	}
	
	
	public void setParkOrUnpark(int p) {
		this.parkOrUnpark = p;
	}
	
	public Vehicle(String licensePlate)
	{
		this.licensePlate = licensePlate;
		
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public VehicleType getvType() {
		return vType;
	}
	
}