# ParkingLotApplication

##Description 

This application implements a ParkingLot using multi-threading. It blocks on number of entries, exits and capacity. It can handle general and handicapped vehicles. Some salient features of the project are: 

* It is extensible -- It is easy to add more kinds of vehicles by extending the `Vehicle` class. 
* It is configurable -- number of entries, exits, capacity, number of general and handicapped parking slots, number of threads are all configurable in the properties file. 

##How to build the project 

* Easy! pom.xml file is attached. just do mvn package and it would also run the unit tests to make sure nothing has gone wrong in the interim. after that, do 
	java -jar target/parkinglotapplication-1.0-SNAPSHOT.jar

* This JAR file would simulate usage of the parking lot by generating requests that can be parking/unparking for general/handicapped vehicles, chosen at random (with equal likelihood). The values for the number of threads, entries, exits  and other aspects of the parking lot are taken from the properties file. 


##Condition variables

* To make sure multiple threads can be in sync and work together, this project uses condition variables. Condition variables do the same job as `synchronized` blocks of code. However, one condition variable can handle the wait, notify, notifyAll and signal concepts by using await and signal keywords. 

To know more about condition variables, the [documentation](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html) is quite useful. 

##Feedback 

* I would love to know your feedback about this project! This was done partly to see how to move beyond a traditional parking lot question and see other real world use cases in code. It was also good to use condition variables to handle multiple threads. 
