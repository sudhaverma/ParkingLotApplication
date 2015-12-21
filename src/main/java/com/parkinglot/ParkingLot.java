package main.java.com.parkinglot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import main.java.com.exceptions.VehicleAlreadyParkedException;
import main.java.com.exceptions.VehicleNotParkedException;
import main.java.com.vehicle.GeneralVehicle;
import main.java.com.vehicle.HandicappedVehicle;
import main.java.com.vehicle.Vehicle;
import main.java.com.vehicle.Vehicle.VehicleType;


public class ParkingLot implements IParkingLot {
	
	public boolean isVehicleInParkingLot(Vehicle v) {
		if (v.getvType() == VehicleType.General) {
			return filledGeneralSlots.containsKey(v);
		} else {
			return filledHandicappedSlots.containsKey(v);	
		}
	}
	
	
	
	
	private List<Vehicle> requests;
	private int requestIndex;
	
	public void setRequests(List<Vehicle> reqs) {
		this.requests = new ArrayList<Vehicle>();
		this.requests = reqs;
	}

	public Vehicle getNextRequest() {
		Vehicle v = requests.get(requestIndex);
		requestIndex++;
		return v;
	}


	//number of entries,exits and capacity of the parking lot
	private int numEntries;
	private int numExits;
	private int capacity;
	
	//To read configuration data from properties file
	InputStream inputStream;
	
	//total number of general and handicapped slots
	private int totalGeneralSlots;
	private int totalHandicappedSlots;
	
	
	//To keep track of how many general slots,handicapped slot,entries and exits have already been used in the parking lot
    private int filledGeneralSlotsIndex;
	private int filledHandicappedSlotsIndex;
	private int entriesIndex;
	private int exitsIndex;
	
	private int capacityIndex;

	
		
	public ParkingLot() throws IOException {
		//getPropValues() will configure number of entries,number of exits,number of general slots and number of handicapped slots for the parking lot
		this.getPropValues();
		this.capacityIndex = 0;

	}

	
	
	public int getNumberOfEntriesLeft() {
		// Calculates number of entries vacant
		int entriesLeft = capacity - entriesIndex;
		return entriesLeft;
	}

	
	public int getNumberofExitsLeft() {
		// Calculates number of exits vacant
		int exitsLeft = capacity - exitsIndex;
		return exitsLeft;
	}

	public int getNumberofParkingSlotsLeft() {
		//Calculates number of slots vacant
		int slotsLeft = capacity - capacityIndex;
		return slotsLeft;
	}
	
	
/* Logic for handling Parking and UnParking logic */
	final Lock lock = new ReentrantLock();
	final Condition notEmpty = lock.newCondition();
	final Condition notFull = lock.newCondition();
	final Condition noEntry = lock.newCondition();
	final Condition noExit = lock.newCondition();
	final Condition notGeneralFull = lock.newCondition();
	final Condition notHandicappedFull = lock.newCondition();
	
	private Map<Vehicle, Integer> filledGeneralSlots = new HashMap<Vehicle, Integer>();
	private Map<Vehicle, Integer> filledHandicappedSlots = new HashMap<Vehicle, Integer>();
	private Map<Vehicle , Integer> availableSlots = new HashMap<Vehicle,Integer>(totalGeneralSlots + totalHandicappedSlots);
	
	private int numberOfParkingThreads;
	private int numberofUnparkingThreads;
	private int numberRequests;

	/*
	 * Park method will check 
	 * 1. if all entries are taken. if not, go to 2. 
	 * 2. if the vehicle is already parked then throw custom exception saying vehicle already parked,so cannot be parked again. if not, park it.
	 * 3. park vehicle. update all relevant indexes. 
	 * @see com.parkinglot.IParkingLot#unPark(com.vehicle.Vehicle)
	 */
	public void park(Vehicle v) throws InterruptedException
	{
		System.out.println("Park method called : "+Thread.currentThread().getName());
		lock.lock();
		try
		{
			
			if(v.getvType() == VehicleType.General)
			{
				if(filledGeneralSlots.containsKey(v))
				{
					throw new VehicleAlreadyParkedException();
				}
				
					
			}
			else
			{
				if(filledHandicappedSlots.containsKey(v))
				{
					throw new VehicleAlreadyParkedException();
				}
			}
			if (numEntries == entriesIndex)
			{
				System.out.println("Vehicle No. "+v.getLicensePlate()+" is waiting to be entered in the parking lot by - "+Thread.currentThread().getName());
				noEntry.await();
			}
			else
			{
				entriesIndex++;
				noEntry.signal();
			}
			
			while (availableSlots.size() == totalGeneralSlots + totalHandicappedSlots)
			{
				System.out.println("Waiting to be parked thread available slots - " + Thread.currentThread().getName());
				notFull.await();
			}
			notFull.signal();
			
			if(v.getvType() == VehicleType.General)
			{
				while(filledGeneralSlots.size() == totalGeneralSlots)
				{
					System.out.println("Vehicle No. "+v.getLicensePlate()+" is waiting to be parked in General slots - " + Thread.currentThread().getName());
					notGeneralFull.await();
				}
				filledGeneralSlots.put((GeneralVehicle) v , ++filledGeneralSlotsIndex);
				notGeneralFull.signal();
				System.out.println("Vehicle No. " + v.getLicensePlate()+" is parked in General slot. Size of general slots filled is "+ filledGeneralSlotsIndex + ". Available general slots ->"+ (totalGeneralSlots - filledGeneralSlots.size()));
			}
			else
			{
				while(filledHandicappedSlots.size() == totalHandicappedSlots)
				{
					System.out.println("Vehicle No. "+v.getLicensePlate()+" is waiting to be parked in Handicapped slots - " + Thread.currentThread().getName());
					notHandicappedFull.await();
				}
				filledHandicappedSlots.put((HandicappedVehicle) v, ++filledHandicappedSlotsIndex);
				notHandicappedFull.signal();
				System.out.println("Vehicle No. "+v.getLicensePlate()+" is parked in Handcapped slot . Size of Handicapped slots filled is " + filledHandicappedSlotsIndex + ". Available Handicapped slots ->" + (totalHandicappedSlots - filledHandicappedSlots.size()));
			}
			capacityIndex++;
			entriesIndex--;
				
		}
		catch(VehicleAlreadyParkedException vape)
		{
			System.out.println("Vehicle No."+v.getLicensePlate()+" is already parked.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
		
	}
	
	/*
	 * unPark method will check 
	 * 1. if all exits are taken. if not, go to 2. 
	 * 2. if the car is already parked. if not, can't be UnParked.  request invalid. if yes, go to 3.
	 * 3. UnPark vehicle. update all relevant indexes. 
	 * @see com.parkinglot.IParkingLot#unPark(com.vehicle.Vehicle)
	 */
	public void unPark(Vehicle v) throws InterruptedException  {
		System.out.println("UnPark method called :"+Thread.currentThread().getName());
	    lock.lock();
	     try
			{
	    	    if (v.getvType() == VehicleType.General) {
	    	    	if (!filledGeneralSlots.containsKey(v)) {
	    	    		throw new VehicleNotParkedException();
	    	    	}
	    	    }
	    	    else {
	    	    	if (!filledHandicappedSlots.containsKey(v)) {
	    	    		throw new VehicleNotParkedException();
	    	    	}
	    	    }
	    	    
	    	    //Now we have confirmed that car is, in fact, parked. so check if exits are available
				if (numExits == exitsIndex)
				{
					System.out.println("All the exits are full. Vehicle No. " + v.getLicensePlate() + " is waiting to be Unparked from the parking lot - " + Thread.currentThread().getName());
					noExit.await();
				}
				
				else
				{
					exitsIndex++;
					noExit.signal();
				}
				
				System.out.println("Vehicle to be Unparked from the parking lot is " + v.getLicensePlate() + "Size of parking lot filled :"+capacityIndex);
			    				
				if(v.getvType() == VehicleType.General)
				{
		            filledGeneralSlots.remove(v);
					System.out.println("General Vehicle unparked is "+v.getLicensePlate() + ". Available general slots ->" + (totalGeneralSlots - filledGeneralSlots.size()));
				}
				else
				{
					filledHandicappedSlots.remove(v);
					System.out.println("Handicapped vehicle unparked " + v.getLicensePlate() + ". Available Handicapped slots -> " + (totalHandicappedSlots - filledHandicappedSlots.size()));
				}
				capacityIndex--;
				exitsIndex--;
					
			}
	        catch(VehicleNotParkedException vnpe)
		     {
		        	System.out.println("Vehicle No."+v.getLicensePlate()+" was never parked, so cannot be unparked");
		     }
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				lock.unlock();
			}
			
	   }


	public void finished() {
		// Called at the end to show that application has completed execution
		System.out.println("Finished executing all the worker threads");
		
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	
	/*
	 * method to configure properties file
	 */
	public void getPropValues() throws IOException {
		try
		{
		Properties prop = new Properties();
		String propFileName = "config.properties";
		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		this.numEntries = Integer.parseInt(prop.getProperty("numEntries"));
		this.numExits = Integer.parseInt(prop.getProperty("numExits"));
		this.capacity = Integer.parseInt(prop.getProperty("capacity"));
		this.totalGeneralSlots = Integer.parseInt(prop.getProperty("availableGeneralSlots"));
		this.totalHandicappedSlots = Integer.parseInt(prop.getProperty("availableHandicappedSlots"));	
		this.numberOfParkingThreads = Integer.parseInt(prop.getProperty("numberofParkingThreads"));
		this.numberofUnparkingThreads = Integer.parseInt(prop.getProperty("numberOfUnparkingThreads"));
		this.numberRequests = Integer.parseInt(prop.getProperty("numRequests"));
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			inputStream.close();
		}
		
	}

	/*
	 * for getting number of worker parking and unparking threads
	 */
	public int getNoOfParkingThread()
	{
		return numberOfParkingThreads;
		
	}
	
	public int getNoOfUnParkingThread()
	{
		return numberofUnparkingThreads;
	}
	
	public int getNumRequests() 
	{ 
		return numberRequests;
	}
	



}
