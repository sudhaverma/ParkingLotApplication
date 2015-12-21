package main.java.com.parkinglot;

import main.java.com.vehicle.Vehicle;

public class ParkingThread implements Runnable{

	private ParkingLot pLot;
	private Vehicle v;
	
	public ParkingThread(ParkingLot pLot) {
		this.pLot = pLot;
	}
	
	
	@Override
	public void run() {
		// Logic of parking any vehicle in Parking lot
		 try {
			while (true) {
				//sleep for 100 milliseconds. wake up. get nextRequest. fulfill it. 
				Thread.sleep(100);
				try { 
					v = pLot.getNextRequest();
					if (v.getParkOrUnpark() == 0) { //0 means park 
						pLot.park(v);
				}
				}
				catch (Exception e) {
					System.out.println("Got an empty request.");
					continue;
				}
			
			}
		}
        catch (InterruptedException e) {
         	e.printStackTrace();
        }
	}

	
}
