import java.util.Scanner;

/**
 	* RIPSimulator class simulates the behavior of RIP in an autonomous system. <br>
 	* There are six routers (R1,R2 ... R6) and seven networks (N1, N2 ... N7). <br>
 	* Ri will be connected to Ni and Ni+1 (0 < i < 7). <br>
 	* The order of advertisement is frist R1, and next R2, and next R3, and ... and finally R6. <br>
	* The program first displays the initial state of each routing table. <br>
	* Then, it also displays the information of each routing table after 1st iteration. <br>
	* After the 1st iteration, it will ask the user to continue the simulation. <br>
	* The simulation automatically ends if it detects convergence of the autonomous system.

	* @author Naoya Hayashi
	* <dt><b>Student No.:</b><dd>
	* 301233985
	* @date November 2nd, 2015
	* @version 1.0
	*/
public class RIPSimulator {

	/**
		* Main method to execute the RIP simulation in the AS. <br>
        * @param args args is not used in this program
        */
	public static void main(String[] args) {
		//final int NUM_OF_ROUTERS = 6;
		
		System.out.println("The RIP simulation starts...");
		
		// Construct tables for each router and initialize them
		Table R1 = new Table();
		R1.init(1);
		Table R2 = new Table();
		R2.init(2);
		Table R3 = new Table();
		R3.init(3);
		Table R4 = new Table();
		R4.init(4);
		Table R5 = new Table();
		R5.init(5);
		Table R6 = new Table();
		R6.init(6);
		
		// Each string will store the routing table information in the previous iteration
		// This is necessary to determine whether autonomous system (AS) converges or not.
		// i.e.) If all table information stays unchanged after one iteration, it implies that the AS converged.
		String previousR1;
		String previousR2;
		String previousR3;
		String previousR4;
		String previousR5;
		String previousR6;
		
		char command = 'y'; // Store user's input to continue the next iteration of this RIP simulation
		int count = 1; // This number keeps track of the number of iteration
		Scanner input = new Scanner(System.in);
		
		// Print initial state of routing information for each table
		System.out.println("--------- The initial state of each routing table ---------");
		printTable(R1);
		printTable(R2);
		printTable(R3);
		printTable(R4);
		printTable(R5);
		printTable(R6);
		
		while(command == 'y' || command == 'Y'){
			// Store R2's routing information because it will be updated by R1's advertisement
			previousR2 = R2.toString();
			// R1 advertises (The table of R2 will change)
			advertise(R1, R2, null);
			
			// Store routing information of R1 and R3 because they will be updated by R2's advertisement
			previousR1 = R1.toString();
			previousR3 = R3.toString();
			// R2 advertises (The table of R1 and R3 will change)
			advertise(R2, R1, R3);
			
			// Store routing information of R2 and R4 because they will be updated by R3's advertisement
			previousR2 = R2.toString();
			previousR4 = R4.toString();
			advertise(R3, R2, R4);
			
			// Store routing information of R3 and R5 because they will be updated by R4's advertisement
			previousR3 = R3.toString();
			previousR5 = R5.toString();
			advertise(R4, R3, R5);
			
			// Store routing information of R4 and R6 because they will be updated by R5's advertisement
			previousR4 = R4.toString();
			previousR6 = R6.toString();
			advertise(R5, R4, R6); 
			
			// Store R5's routing information because it will be updated by R6's advertisement
			previousR5 = R5.toString();
			advertise(R6, R5, null);
			
			// Check the convergence. If the condition statement is true, it means that the AS converges.
			if(previousR1.equals(R1.toString()) && previousR2.equals(R2.toString()) && previousR3.equals(R3.toString()) 
					&& previousR4.equals(R4.toString()) && previousR5.equals(R5.toString()) && previousR6.equals(R6.toString())) {
				System.out.println("The Autonomous System has converged.");
				break;
			}
			
			// Print routing table information for each table after one iteration
			System.out.printf("--------- The routing table information after %sth operation ---------%n", count);
			printTable(R1);
			printTable(R2);
			printTable(R3);
			printTable(R4);
			printTable(R5);
			printTable(R6);
			
			// Ask for the user input (y/n). If y is typed, the program will execute the next iteration of advertisement.
			do{
				System.out.print("Repeat the next operation? (y/n): ");
				command = input.next().charAt(0);
				if(!(command == 'y' || command == 'n' || command == 'Y' || command == 'N')){
					System.out.println("Invalid input. Type only 'y' or 'n'.");
				}
			}while(!(command == 'y' || command == 'n' || command == 'Y' || command == 'N'));
			System.out.println("");
			count++;
		}
		System.out.println("The simulation ends...");
		input.close(); // close the scanner
		System.exit(0);
	}
	
	
	/**
		* Simulates advertisement of a router. <br>
		* @param advertisingTable a table of advertising router
		* <dt><b>Precondition:</b><dd>
		* advertisingTable must not be null.
		* @param advertisedTable1 a table of advertised router
		* <dt><b>Precondition:</b><dd>
		* advertisedTable must not be null.
		* @param advertisedTable2 a table of advertised router
	 	*/
	public static void advertise(Table advertisingTable, Table advertisedTable1, Table advertisedTable2){
		// Advertise to one adjacent router
		advertisedTable1.update(advertisingTable);
		// If another adjacent Router exists, advertise to it, too.
		if(advertisedTable2 != null){
			advertisedTable2.update(advertisingTable);
		}
	}

	
	/**
		* Print routing table information in an aligned format. <br>
    	* @param table Routing Table
    	* <dt><b>Precondition:</b><dd>
		* table must not be null.
    	*/
	public static void printTable(Table table){
		System.out.printf("Routing Table for %s: %n", table.getRouterName());
		System.out.println(table.toString());
	}

}
