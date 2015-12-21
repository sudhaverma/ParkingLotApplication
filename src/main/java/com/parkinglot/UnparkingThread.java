package main.java.com.parkinglot;


import java.util.Properties;

import main.java.com.vehicle.Vehicle;

public class UnparkingThread implements Runnable {

	private ParkingLot pLot;
	private Vehicle v;
	
	public UnparkingThread(ParkingLot pLot) {
		this.pLot = pLot;
	}

	
	@Override
	public void run() {
		//logic for unParking any vehicle from the Parking lot
		try {
			while (true) {
				//sleep for 100 milliseconds. wake up. get nextRequest. fulfill it. 
				Thread.sleep(100);
				try { 
					v = pLot.getNextRequest();
					if (v.getParkOrUnpark() == 1) { //1 means unpark 
						pLot.unPark(v);
				}
				}
				catch (Exception e) {
					System.out.println("Got an empty request. going to sleep again");
					continue;
				}
				
			}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
	}

}
