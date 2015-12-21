package test.java.com.parkinglot;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import main.java.com.exceptions.VehicleAlreadyParkedException;
import main.java.com.exceptions.VehicleNotParkedException;
import main.java.com.parkinglot.ParkingLot;
import main.java.com.vehicle.GeneralVehicle;
import main.java.com.vehicle.HandicappedVehicle;

public class TestParkUnpark {

	private GeneralVehicle gv;
	private HandicappedVehicle hv;
	private ParkingLot pLot;
	
	
	@Before 
	public void setUp() throws IOException {
		gv = new GeneralVehicle("abcd");
		hv = new HandicappedVehicle("xwyz");
		pLot = new ParkingLot();
	}
	
	@Test 
	public void testPark() throws IOException, InterruptedException {
		pLot.park(gv);
		assert pLot.isVehicleInParkingLot(gv) == true;
		assert pLot.getNumberofParkingSlotsLeft() == (pLot.getCapacity() - 1);
		pLot.unPark(gv);
	}
	
	@Test
	public void testUnPark() throws IOException, InterruptedException {
		pLot.park(gv);
		pLot.unPark(gv);
		assert pLot.isVehicleInParkingLot(gv) == false;
		assert pLot.getNumberofParkingSlotsLeft() == (pLot.getCapacity());
	}
	
}
