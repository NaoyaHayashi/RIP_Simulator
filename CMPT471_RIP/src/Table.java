/**
 	* Table class represents the routing table of a router in an autonomous system. <br>
 	* It contains the following instance variables: <br>
 	* currentSize - the number of routes in the Table <br>
	* routerName - the name of the router which owns the table <br>
	* routes - a set of routes which are stored in the table 
	
	* @author Naoya Hayashi
	* <dt><b>Student No.:</b><dd>
	* 301233985
	* @date November 2nd, 2015
	* @version 1.0
	*/
public class Table {
	// Inner class 
	// Route object contains destination, metric and nextHop for each route.
	private class Route{
		private String destination; // the destination network
		private int metric; // the number of hops
		private String nextHop; // the next hop router
		
		// Default constructor of this inner class, Route
		private Route(){
			destination = null;
			metric = 0;
			nextHop = "none";
		}
	}
	
	// The capacity of routes[] 
	// 100 is my arbitrary choice
	final int TABLE_CAPACITY = 100;
	// Initial default value for metric variable
	final int DEFAULT_METRIC = 1;
	
	// Instance variables of Table class
	private int currentSize;
	private String routerName;
	private Route[] routes;
	
	/**
	 * Default Constructor (no parameter).
	 */
	public Table() {
		routes = new Route[TABLE_CAPACITY];
		for(int i=0; i<TABLE_CAPACITY; i++){
			routes[i] = new Route();
		}
		currentSize = 0;
		routerName = null;
	}
	
	/**
	 * Does the initial setup for a routing table.
	 * This method will set up the routing information of networks which are DIRECTLY connected to the router.
	 * @param routerNumber the number that identifies a router (1 for R1, 2 for R2 etc.)
	 */	
	public void init(int routerNumber){
		routerName = "R" + routerNumber;
		
		// Set up routing information for Ni (i is routerNumber)
		routes[0].destination = "N" + routerNumber;
		routes[0].metric = DEFAULT_METRIC;
		routes[0].nextHop = "none";
		currentSize++; // Increment the size of table because one route is added
		
		// Set up routing information for Ni+1
		routes[1].destination = "N" + (routerNumber + 1);
		routes[1].metric = DEFAULT_METRIC;
		routes[1].nextHop = "none";
		currentSize++; // Increment the size of table because one route is added
		
		return;
	}
	
	/**
	 * Updates routes of this table based on the advertisement from an advertising table.
	 * @param table an advertising table
	 * <dt><b>Precondition:</b><dd>
	 * advertisedTable must not be null.
	 */	
	public void update(Table advertisingTable){
		int i = 0;
		int j = 0;
		for(; i < advertisingTable.currentSize; i++){
			for(; j < currentSize; j++){
				// condition: if same destination appears in both table, compare the metrics (# of hops) and if 
				// advertising route is equal or longer, there is no update.
				if(routes[j].destination.equals(advertisingTable.routes[i].destination) && 
						routes[j].metric <= (advertisingTable.routes[i].metric + 1)){
					break;
				}
				// if advertising route is shorter, the table must be updated.
				else if(routes[j].destination.equals(advertisingTable.routes[i].destination) && 
						routes[j].metric > (advertisingTable.routes[i].metric + 1)){
					//update
					routes[j].destination = advertisingTable.routes[i].destination;
					routes[j].metric = advertisingTable.routes[i].metric + 1;
					routes[j].nextHop = advertisingTable.routerName;
					currentSize++;
					break;
				}
			}
			// If j becomes equal to currentSize, advertisingTable.route[i] is new to the advertised router,
			// so the new route information must be added.
			if(j == currentSize){
				routes[currentSize].destination = advertisingTable.routes[i].destination;
				routes[currentSize].metric = advertisingTable.routes[i].metric + 1;
				routes[currentSize].nextHop = advertisingTable.routerName;
				currentSize++;
			}
			// reset j
			j = 0;
		}
	}
	
	/**
	 * Returns routing table information in a formatted style.
	 * @return a String which stores all routing information of a table
	 */	
	public String toString(){
		String str = "";
		// Formatting the title of each column.
		str = str + String.format("%-15s %-15s %-15s %n", "Destination", "Metric", "Next Hop");
		// Formatting each routing information and concatenate it to str
		for(int i=0; i<currentSize; i++){
			str = str + String.format("%-15s %-15d %-15s %n", routes[i].destination, routes[i].metric, routes[i].nextHop);
		}
		return str;
	}
	
	/**
	 * Returns a router name which holds this table.
	 * @return router name (eg. R1, R2 etc)
	 */		
	public String getRouterName(){
		// Check if router name is null. If so, it provides a warning.
		if(routerName == null){
			System.out.println("Warning: The router name is null...");
		}
		return routerName;
	}
	
}
