package main.java.com.parkinglot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyValues {

	private static ParkingLot pLot;
	InputStream inputStream;
	String result;
	public void getPropValues() throws IOException {
		// method to configure properties file
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
		
		
		int entries = Integer.parseInt(prop.getProperty("numEntries"));
		int exits = Integer.parseInt(prop.getProperty("numExits"));
		int capacity = Integer.parseInt(prop.getProperty("capacity"));
		int availableGeneralSlots = Integer.parseInt(prop.getProperty("availableGeneralSlots"));
		int availableHandicappedSlots = Integer.parseInt(prop.getProperty("availableHandicappedSlots"));
		
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

}
