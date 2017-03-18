// Christian Jarani
// Professor Farnan
// CS 1501
// Project 3

import java.util.Scanner;

public class CarTracker {

   // Globals
	private static IndexMinPQ 	pqPrice = new IndexMinPQ(100,'p');		// Will hold cars which are organized in priority by price
	private static IndexMinPQ pqMileage = new IndexMinPQ(100,'m');		// Will hold same cars, but organized in priority by mileage
	private static Scanner 			 kb = new Scanner(System.in);
	private static int 			numCars = 0;
	//private static Car[];
	
	public static void main (String[] args) {

		System.out.println("\n\n ----- Welcome to Chris' Car Service! -----\n");
		System.out.println("   It's our mission to help you find the   \n   best car at the lowest price/mileage.\n\n");

		int choice;

		while(true) {
			System.out.println("What would you like to do?");
			System.out.println("\t1. Add a Car\n\t2. Update a Car\n\t3. Remove a Car\n\t4. Find Lowest Price Car\n\t5. Find Lowest Mileage Car\n");
			
			while (true) {
				System.out.print("Select a number to choose an option: ");
				choice = kb.nextInt();
				if(choice < 1 || choice > 5) 
				 	System.out.println("\n!! Invalid choice - please select an option between 1 and 5. !!\n");
				else {
					kb.nextLine();
					break;
				}
			}    
				 if (choice == 1) add();
			else if (choice == 2) update();
			else if (choice == 3) remove();
			else if (choice == 4) getLowestPrice();
			else if (choice == 5) getLowestMileage();
		}
		
	}

	private static void add() {
		Car 	temp = new Car();
		String 	str;
		int 	j;

	    while(true) {
			System.out.print("Enter the VIN: "); str = kb.nextLine();
				 if (str.contains("Q") || str.contains("I") || str.contains("O") || str.contains("q") || str.contains("i") || str.contains("o"))
					System.out.println("\n!! Error - VIN cannot contain Q (q), I (i), or O (o) !!\n");
			else if (str.length() != 17)
					System.out.println("\n!! Error - VIN must be of 17 characters in length !!\n");
			else {
				break;
			} 	
		}
											     temp.setVin(str.toUpperCase());
		System.out.print("Enter the make: ");    str = kb.nextLine();
											     temp.setMake(str.toUpperCase());
		System.out.print("Enter the model: ");   str = kb.nextLine();
											     temp.setModel(str.toUpperCase());
		System.out.print("Enter the colour: ");  str = kb.nextLine();
											     temp.setColour(str.toUpperCase());
		System.out.print("Enter the price: ");   j = kb.nextInt();
											     temp.setPrice(j);
		System.out.print("Enter the mileage: "); j = kb.nextInt();
											     temp.setMileage(j);

		  pqPrice.insert(numCars,temp);				// Because both pq's will reference the same Car object, changes made to that car in either PQ will be reflected in both.
		pqMileage.insert(numCars,temp);
		System.out.println("Car added successfully.\n"); numCars++;
		return;
	}

	private static void update() {

		String 	vinToFind;					// VIN of car that we want to find/update
		String 		  str;					// Temp for holding updated info for VIN/make/model/colour
		int 	   choice;					// Selection for field to update
		int 			j;					// Temp for holding updated info for price/mileage
		
		while(true) {
			System.out.print("Please enter the VIN of the car you wish to update: ");
			vinToFind = kb.nextLine();
				 if(vinToFind.contains("Q") || vinToFind.contains("I") || vinToFind.contains("O") || vinToFind.contains("q") || vinToFind.contains("i") || vinToFind.contains("o"))
					System.out.println("\n!! Error - VIN cannot contain Q (q), I (i), or O (o) !!\n");
			else if(vinToFind.length() != 17)
					System.out.println("\n!! Error - VIN must be of 17 characters in length !!\n");
			else {
				vinToFind.toUpperCase();
				kb.nextLine();
				break;
			}
		}

		Car temp;	// Holds info of car we want to update
   		for(int i : pqPrice.pq) {
			if (pqPrice.carAt(i).getVin().compareTo(vinToFind) == 0) {
		   		System.out.println("What field would you like to update?");
				System.out.println("\t1. VIN\n\t2. Make\n\t3. Model\n\t4. Colour\n\t5. Price\n\t6. Mileage\n");
		   		while (true) {
					System.out.print("Select a number to choose an option: ");
					choice = kb.nextInt();
					if(choice < 1 || choice > 6) 
					 	System.out.println("\n!! Invalid choice - please select an option between 1 and 6. !!\n");
					else {
						kb.nextLine();
						break;
					}
				}
				switch (choice) {	// Update temp using setter method corresponding to the chosen field
					case 1: while(true) {
								System.out.print("Please enter the new VIN: "); str = kb.nextLine();
									if (  str.contains("Q") ////////////////////////////////////////////
									   || str.contains("I") ///////////////////////////////////////////
									   || str.contains("O") //////////////////////////////////////////
									   || str.contains("q") /////////////////////////////////////////
									   || str.contains("i") ////////////////////////////////////////
									   || str.contains("o")  ) System.out.println("\n!! Error - VIN cannot contain Q (q), I (i), or O (o) !!\n");
							   else if ( str.length() != 17  ) System.out.println("\n!! Error - VIN must be of 17 characters in length !!\n");
							   else	    					   break;
							}
							temp = pqPrice.carAt(i);
							temp.setVin(str.toUpperCase());
							break;	 
					case 2: 	    		   	  System.out.print("Please enter the new make: ");															
					case 3: 	 if (choice == 3) System.out.print("Please enter the new model: ");
					case 4: 	 if (choice == 4) System.out.print("Please enter the new colour: ");
							 str = kb.nextLine();
					 		temp = pqPrice.carAt(i);
								 if (choice == 2) temp.setMake(str.toUpperCase());
						    else if (choice == 3) temp.setModel(str.toUpperCase());
						    else if (choice == 4) temp.setColour(str.toUpperCase());
						    break;
					case 5: System.out.print("Please enter the new price: $"); j = kb.nextInt();
					 		 	 if (j < pqPrice.carAt(i).getPrice())
					 		 		 pqPrice.decrease(i,j);
					 	    else if (j > pqPrice.carAt(i).getPrice())
					 		 		 pqPrice.increase(i,j);
					 	    else 	 System.out.println("This price is the same as the car's current price.\n");
					 	    break;
					case 6: System.out.print("Please enter the new mileage: "); j = kb.nextInt();
					 		 	 if (j < pqMileage.carAt(i).getMileage())
					 		 		 pqMileage.decrease(i,j);
					 	    else if (j > pqMileage.carAt(i).getMileage())
					 		 		 pqMileage.increase(i,j);
					 	    else 	 System.out.println("This mileage is the same as the car's current mileage.\n");
					 	    break;
				   default: throw new IllegalArgumentException("!! LOGIC FAULT - option does not exist !!");
				}
				break;
			}
			System.out.println("Value updated successfully.\n");
		}
	}			
	private static void remove() {
		String 	vinToFind;					// VIN of car that we want to find/update
		
		while(true) {
			System.out.print("Please enter the VIN of the car you wish to update: ");
			vinToFind = kb.nextLine();
				 if(vinToFind.contains("Q") || vinToFind.contains("I") || vinToFind.contains("O") || vinToFind.contains("q") || vinToFind.contains("i") || vinToFind.contains("o"))
					System.out.println("\n!! Error - VIN cannot contain Q (q), I (i), or O (o) !!\n");
			else if(vinToFind.length() != 17)
					System.out.println("\n!! Error - VIN must be of 17 characters in length !!\n");
			else {
				vinToFind.toUpperCase();
				break;
			}
		}

		for(int i : pqPrice.pq) {
			if (pqPrice.carAt(i).getVin().compareTo(vinToFind) == 0) {
				pqPrice.delete(i);
				pqMileage.delete(i);
				System.out.println("Car was removed successfully.\n");
				break;
			}
		}

	}
	private static void getLowestPrice() {
		System.out.println("The lowest-priced vehicle is:\n\tVIN: "+pqPrice.minCar().getVin()+"\n\tMake: "+pqPrice.minCar().getMake()+"\n\tModel: "+pqPrice.minCar().getModel()+"\n\tColour: "+pqPrice.minCar().getColour()+"\n\tPrice: $"+pqPrice.minCar().getPrice()+"\n\tMileage: "+pqPrice.minCar().getMileage()+"\n");
	}
	private static void getLowestMileage() {
		System.out.println("The lowest-priced vehicle is:\n\tVIN: "+pqMileage.minCar().getVin()+"\n\tMake: "+pqMileage.minCar().getMake()+"\n\tModel: "+pqMileage.minCar().getModel()+"\n\tColour: "+pqMileage.minCar().getColour()+"\n\tPrice: $"+pqMileage.minCar().getPrice()+"\n\tMileage: "+pqMileage.minCar().getMileage()+"\n");
	}
	private static void getLowestPriceByMake() {

	}
	private static void getLowestPriceByModel() {

	}
	private static void getLowestMileageByMake() {

	}
	private static void getLowestMileageByModel() {
		
	}
}

