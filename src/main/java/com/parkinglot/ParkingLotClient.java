package main.java.com.parkinglot;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.java.com.exceptions.VehicleNotParkedException;
import main.java.com.vehicle.GeneralVehicle;
import main.java.com.vehicle.HandicappedVehicle;
import main.java.com.vehicle.Vehicle;

public class ParkingLotClient implements Runnable {
	
	private static ParkingLot pLot;
	
	private static List<Vehicle> requestList;
	
	private static Random rand;
	private static Integer gh;
	private static Integer pu;
	private static String vType;
	private static String licensePlate;
	private static Thread requestThread;
	
	
	public static void main(String args[])  throws InterruptedException,IOException {
		pLot = new ParkingLot();
		requestThread = new Thread();
		int numRequests = pLot.getNumRequests();
		
		requestList = new ArrayList<Vehicle>(numRequests);
		//now we will create N requests which will be of them form 0/1:G/H:license plate. 0 = park, 1 = unpark
		//G = General, H = handicapped. these requests will be sent to parkingLot class, one every few ms. 
		
		rand = new Random();
		
		for (int i = 0; i < numRequests; i++) {
			gh = rand.nextInt(2);//general or handicapped. 
			pu = rand.nextInt(2);//park or unpark
			if (gh == 0) {
				vType = "G";
			}
			else {
				vType = "H";
			}
			Integer temp = rand.nextInt(1000);
			licensePlate = temp.toString();
			if (gh == 0) {
				GeneralVehicle gv = new GeneralVehicle(licensePlate);
				gv.setParkOrUnpark(pu);
				requestList.add(gv);
			}
			else {
				HandicappedVehicle hv = new HandicappedVehicle(licensePlate);
				hv.setParkOrUnpark(pu);
				requestList.add(hv);
			}	
		}
		pLot.setRequests(requestList);
		
		/*
		 * Creating threads to handle Parking logic and Unparking logic
		 */
		Thread parkThread = null;
		Thread UnparkThread = null;
		int i,j;
		int ParkingThread = pLot.getNoOfParkingThread();
		int UnParkingThread = pLot.getNoOfUnParkingThread();
		for(i = 0 ;i <= ParkingThread ;i++)
		{
		parkThread = new Thread(new ParkingThread(pLot), "Parking -Worker Thread"+i);
		parkThread.start();
     	}
		for(j=0; j<= UnParkingThread; j++)
		{
		UnparkThread = new Thread(new ParkingThread(pLot), "UnParking -Worker Thread"+j);
		UnparkThread.start();
		
		}
		for(i = 0 ;i <= ParkingThread ;i++)
		{
			parkThread.join();
		}
		for(j=0; j <= UnParkingThread; j++)
		{
			UnparkThread.join();
			
		}
		
        pLot.finished();
	}

	//This contains logic of request thread run() which is responsible to run Parking thread and UnParking thread in the application
	@Override
	public void run() {
		int index = 0;
		int parkOrUnpark;
		
		while (true) {
			parkOrUnpark = rand.nextInt(2);
			if (parkOrUnpark == 0) {
				try {
					pLot.park(requestList.get(index));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				index++;
			}
			else {
				try {
					pLot.unPark(requestList.get(index));
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
		}
		
	}

	
	
}
