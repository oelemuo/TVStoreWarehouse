import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/** 
 * @author Obinna Elemuo
 * CMSY265 - Fall 2023
 * 12/07/2023
 * This program manages a TV store's inventory using a stack-based system. Each TV is given a unique ID and brand name which is read from
 * a text file using a file reader and these brands and ID's are used to represent its place in the warehouse's inventory. The user can perform various actions such as 
 * stocking shelves, filling web orders, re-stocking returns, adding new inventory, viewing current inventory,
 * handling customer purchases, and processing customer checkouts. 
 * Queues are utilized to manage customer transactions in the order they are received, ensuring a 
 * FIFO (First-In-First-Out) system. A newly added feature of this program is its capability to sort the 
 * customer database using Binary Trees and recursive method calls. These Binary Trees provide efficient 
 * management and traversal of customer data, ensuring optimized experiences for tasks such as customer purchase 
 * lookup and data updating. This approach leverages the fast search, insert, and delete operations of Binary Trees 
 * for enhanced performance in data handling. It also supports bulk TV purchases, making it a comprehensive tool 
 * for managing TV store operations.The MaxHeap data structure is utilized to efficiently manage the incoming
 * delivery information. It ensures that TVs with the highest number of units
 * are processed first, facilitating efficient restocking and inventory management.
 * Additionally, the program efficiently manages incoming delivery information using a MaxHeap data structure. 
 * The MaxHeap ensures that TVs with the highest number of units, and the Heap itself is used to represent the Delivery Stop of Customers
 * Finally, the code utilizes Maps with key/value pairs to represent the ID Number of TV along with the Price in order to get return as credit for customer
 * @version 1.0
 */
public class tvStoreWarehouse implements menuOptions {
	
	private static MaxHeap deliveryHeap = new MaxHeap(menuOptions.MAX_DELIVERY);
    
    public static void main(String[] args) throws IOException {
        displayProgramIntro();
        // Instantiate the customerData object
        CustomerData customerData = loadCustomerDataFromFile();
        System.out.println("Loaded " + customerData.getCustomerCount() + "customer.");
        // System.out.println("Loaded " + customerData.getCustomerCount() + " customers.");
        /**
         * Used for understanding the Java Ternary Operator in order to get the ID number of stack. 
         * 
         * Source:
         * "Tutorialspoint: Java Ternary Operator examples." TutorialsPoint, 
         * n.d., https://www.tutorialspoint.com/Java-Ternary-Operator-examples.
         */
        Stack<TV> inventory = loadInitialInventoryFromFile(); // call file reader
        int[] lastIdNumberContainer = { inventory.isEmpty() ? 0 : extractIdNumber(inventory.peek().getID()) };
        // Initialize an empty queue to store Customer objects using a linked list 
        Queue<Customer> customerQueue = new LinkedList<>();
       
        // Call the method to read the TV data and fill the Binary Tree
        BinaryTree binaryTree = readTVDataAndBuildTree();
        //
        

        int choice;
        // cases to make sure user can pick the appropriate choices
        do {
            choice = getMenu();
            switch (choice) {
                case stockShelves:
                    stockShelves(inventory);
                    break;
                case fillWebOrder:
                    fillWebOrder(inventory);
                    break;
                case restockReturn:
                    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
                    restockReturn(inventory, scanner);         // Pass only the inventory stack and the scanner
                    break;
                case restockInventory:
                    restockInventory(inventory, lastIdNumberContainer);
                    break;
                    
                case CUSTOMER_UPDATE:
                	handleCustomerUpdateMenu(customerData);
                	break;
                
                case CUSTOMER_PURCHASE:
                	customerData = handleCustomerPurchase(inventory, customerQueue, customerData, binaryTree, deliveryHeap);
                	
                	break;
                case CUSTOMER_CHECKOUT:
                	handleCustomerCheckout(customerQueue);
                	break;
                case DISPLAY_INVENTORY:
                    displayInventory(inventory);
                    break;
                case DISPLAY_DELIVERY_LIST:
                	displayDeliveryList();
                	customerData = handleCustomerPurchase(inventory, customerQueue, customerData, binaryTree, deliveryHeap);
                case endProgram:
                	if(!customerQueue.isEmpty()) {
                		System.out.println("\nThere are still customers who have not checked out. Please make sure all customers are processed before ending the program.");
                	}
                	else {
                		endProgram(inventory);
                	}
                    break;
            }
        } while (choice != endProgram || !customerQueue.isEmpty());
    }
    // create a static method that loads customer data from the file 'CustFile.txt'
    private static CustomerData loadCustomerDataFromFile() {
        CustomerData customerData = new CustomerData();
        // Input Stream to read file. 
        InputStream in = tvStoreWarehouse.class.getResourceAsStream("/CustFile.txt");
        Scanner fileScanner = new Scanner(in);
        // try catch if file is not found in computer
        try {
            while (fileScanner.hasNextLine()) {
                String customerName = fileScanner.nextLine().trim();
                // if account number is missing from customers name
                if (!fileScanner.hasNextLine()) {
                    System.out.println("Error: Expected account number after customer name '" + customerName + "'.");
                    break;
                }
                String accountNumber = fileScanner.nextLine().trim();
                // Create a new Customer and add it to the CustomerData list
                Customer customer = new Customer(customerName, accountNumber);
                customerData.addCustomer(customer);
            }
        } catch (Exception e) {
            System.out.println("Error reading the customer file: " + e.getMessage());
        } finally { // if there's no file to read, close
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
        
        return customerData;
    }
    // Load TVs from starter file to the stack
    private static Stack<TV> loadInitialInventoryFromFile() {
        Stack<TV> inventory = new Stack<>();
        // Input Stream to read file. 
        InputStream in = tvStoreWarehouse.class.getResourceAsStream("/stack.txt"); 
        Scanner fileScanner = new Scanner(in);
        // try catch if file is not found in computer
        try {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                inventory.push(new TV(line));
            }
        } catch (Exception e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
        return inventory;
    }
    // Display introductory program details.
    public static void displayProgramIntro() {
        System.out.println("Lab 6: Heaps");
        System.out.println("Copyright ©2022 – Howard Community College All rights reserved; Unauthorized duplication prohibited.");
        System.out.println("CMSY 265 TV Inventory Control Program");
        System.out.println(" ");
    }
    // Method to display the menu user and scan choices
    private static int getMenu() {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        // conditional
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("\nMenu Options");
            System.out.println("1 - Stock Shelves");
            System.out.println("2 - Fill Web Order");
            System.out.println("3 - Restock Return");
            System.out.println("4- Restock Inventory");
            System.out.println("5- Customer Update");
            System.out.println("6 - Customer Purchase");
            System.out.println("7 - Customer Checkout");
            System.out.println("8 - Display Delivery List");
            System.out.println("9 - Display Inventory");
            System.out.println("10 - End Program");
            
            System.out.print("Please enter the menu choices: ");
            // try..catch block for validation
            try {
                choice = scan.nextInt();
                // Checking if the choice is between 1 to 6
                if (choice >= 1 && choice <= 10) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid Choice. Please choose a number between 1 and 10.\n");
                }
            } catch(Exception e) { 
                System.out.println("Invalid Choice. Please enter a valid choice from the menu.\n");
                scan.nextLine();
            }
        }
        return choice;
    }
    // Stock shelves by taking TVs off the top of the inventory stack.
 	private static void stockShelves(Stack<TV> inventory) {
 		if(inventory.size() < 5) { // There are not 5 TV's to remove
			System.out.println("Insufficient TV's available! Try again later when there is at least 5 TV's\n");
			return;
		}
 		// for loop to repeat the 'pop' action 5 times
 	    for (int i = 0; i < 5 && !inventory.isEmpty(); i++) {
 	        inventory.pop();
 	        System.out.println("Shelves stocked successfully!\n");
 	    }
 	}
    // Method to remove the top TV from the inventory
    private static Stack<TV> fillWebOrder(Stack<TV> inventory) {
        if (!inventory.isEmpty()) {
            TV tv = inventory.pop(); // 'pop' action only needed once for this method
            System.out.println("Web order filled successfully!\n");
        } 
        else {
            System.out.println("\nThere are no TV's currently in the inventory Try again later when there is at least 1 TV's\r\n");
        }

        return inventory;
    }
    // method to extract number from ID
    private static int extractIdNumber(String id) {
        String[] parts = id.split("-");
        return Integer.parseInt(parts[1]);
    }
    // Re-stock a returned TV by adding a new ID and pushing it onto the stack.
    private static void restockReturn(Stack<TV> inventory, Scanner scanner) throws IOException {
        System.out.print("Enter the path to the 'tvsold.txt' file: ");
        String filePath = scanner.nextLine();
        TVReturn tvReturn = new TVReturn(filePath); // Initialize TVReturn with the user-provided file path

        System.out.print("Please enter the ID number of the returned TV: ");
        String tvId = scanner.nextLine();


        TV returnedTv = tvReturn.processReturn(tvId);
        System.out.println("Returned TV is " + (returnedTv != null ? "found" : "not found")); // debug

        // if ID number is not found
        while (returnedTv == null) {
            System.out.println("Item not found in the TV sold list. Please reenter!");
            tvId = scanner.nextLine();
            returnedTv = tvReturn.processReturn(tvId);
            System.out.println("TV ID entered: " + tvId);

        }
        try {
            System.out.println("Calling getPriceFromID with TV ID: " + tvId);
            double price = getPriceFromID(tvId, filePath);
            double refundAmount = calculateRefundAmount(price);
            System.out.println("The refund amount is: $" + String.format("%.2f", refundAmount));
            inventory.push(returnedTv);
            System.out.println("The TV has been restocked to inventory.");
        } catch (FileNotFoundException e) {
            System.err.println("The system cannot find the file specified: " + filePath);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    // method calculates refund amount
    private static double calculateRefundAmount(double price) {
    	// SALES_TAX is from menuOptions interface
        double taxAmount = price * SALES_TAX;
        return price + taxAmount;
    }
 // Method to get the price of a TV from the 'tvsold.txt' file based on its ID
 private static double getPriceFromID(String tvId, String filePath) throws FileNotFoundException {
     Scanner fileScanner = new Scanner(new File(filePath));

     while (fileScanner.hasNextLine()) {
         String currentId = fileScanner.nextLine().trim();

         if (currentId.equals(tvId)) {
             // Skip brand and model lines
             fileScanner.nextLine();
             fileScanner.nextLine();

             // Read and parse the price line
             if (fileScanner.hasNextLine()) {
                 String priceString = fileScanner.nextLine().trim();
                 fileScanner.close();
                 return Double.parseDouble(priceString);
             }
         }

         // Skip lines to move to the next entry
         for (int i = 0; i < 3 && fileScanner.hasNextLine(); i++) {
             fileScanner.nextLine();
         }
     }
     fileScanner.close();
     return -1.0; // value indicating ID not found
 }
    // Add new TVs to the inventory stack.
    private static void restockInventory(Stack<TV> inventory, int[] lastIdNumberContainer) {
        // Iterate over all TV's in the current stack
        for (int i = 0; i < 5; i++) {
            lastIdNumberContainer[0]++;
            String newTVID = "ABC123-" + lastIdNumberContainer[0];
            inventory.push(new TV(newTVID));
            System.out.println("Inventory restocked successfully.\n" + newTVID);
        }
    }
    // Display the IDs of all TVs in the inventory stack.
    public static void displayInventory(Stack<TV> inventoryStack) {
		System.out.println("The following " + inventoryStack.size()+" TV's are left in inventory\n");
		 // Iterate over all current TVs in the inventory stack
		for(TV tv : inventoryStack) {
			System.out.println(tv.toString());
		}
	}
    // End the program and display current inventory before exiting.
    public static void endProgram(Stack<TV> inventory) {
        displayInventory(inventory);
        System.out.println("Thank you for using the Program. Have a nice day!");
    }
    // function for handling the customer purchase using stacks and queues
    private static CustomerData handleCustomerPurchase(Stack<TV> inventory, Queue<Customer> customerQueue, CustomerData customerData, BinaryTree binaryTree, MaxHeap deliveryHeap) {
    	// call sorted customerData to display customer List
    	customerData = sortCustomerData(customerData);
        customerData.displayCustomer();
        // Check if there are any TVs in inventory
        if (inventory.isEmpty()) {
            System.out.println("Sorry, no TVs are available for purchase.");
            return customerData;
        }

        Scanner scan = new Scanner(System.in);
        String name = "";
        String accountNumber = "";

        // Prompt user for account number
        System.out.print("Please enter the customer's account number (or 'none' if no account): ");
        accountNumber = scan.nextLine().trim();

        // Fetch existing customer from customer data
        Customer existingCustomer = customerData.findCustomer(accountNumber);

        // If 'none' is entered or customer not found, add a new customer
        if (accountNumber.equalsIgnoreCase("none") || existingCustomer == null) {
            System.out.println("Customer not found");

            // Get a unique account number for the new customer
            while (true) {
                System.out.print("Please enter a new account number: ");
                accountNumber = scan.nextLine().trim();
                existingCustomer = customerData.findCustomer(accountNumber);

                if (existingCustomer == null) {
                    break;
                } else {
                    System.out.println("Account number already exists. Please enter a new account number.");
                }
            }

            // Get a valid name for the new customer
            System.out.print("Please enter customer's name: ");
            name = scan.nextLine().trim();
            while (name.isEmpty()) {
                System.out.println("Error: Please provide a valid name.");
                System.out.print("Please enter customer's name: ");
                name = scan.nextLine().trim();
            } 
            // Add the new customer to the customer data
            customerData.addCustomer(new Customer(name, accountNumber));
            // call sorted data
            customerData = sortCustomerData(customerData); 
            System.out.println("New customer added!");
            displayTVOptions(binaryTree); 

        } else {
            // If customer exists, greet the returning customer
            name = existingCustomer.getCustomerName();
            System.out.println("Welcome back " + name + "!");
            displayTVOptions(binaryTree);
        }
        TVType selectedTVType = null;
        while (selectedTVType == null) {
            System.out.print("Please enter the TV make: ");
            String make = scan.nextLine().trim();

            System.out.print("Please enter the TV model: ");
            String model = scan.nextLine().trim();

            selectedTVType = binaryTree.search(make, model); // Use the instance 'binaryTree' to call the method
            if (selectedTVType == null) {
                System.out.println("TV Type not found. Please try again.");
            }
        }
        //Display the brand, model, and cost info for the selected TVType.
        System.out.println("Selected TV Type: " + selectedTVType);


        // Validate and get the number of TVs to purchase
        int numOfTvs = 0;
        while (true) {
            try {
                System.out.print("Enter the number of TVs purchased: ");
                numOfTvs = Integer.parseInt(scan.nextLine().trim());
                if (numOfTvs > 0) {
                    break; 
                } else {
                    System.out.println("The number of TVs must be positive. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        while (numOfTvs <= 0 || numOfTvs > inventory.size()) {
            System.out.println("\nError! The number of TVs available for purchase is between 1 and " + inventory.size());
            System.out.print("Enter the number of TV's purchased: ");
            numOfTvs = scan.nextInt();
        }
        // Store the IDs of the TVs being purchased
        ArrayList<String> purchasedTvIds = new ArrayList<>();
        for (int i = 0; i < numOfTvs; i++) {
            purchasedTvIds.add(inventory.pop().getID());
        }
        // create customer object
        Customer customer = new Customer(name, accountNumber, numOfTvs, purchasedTvIds, selectedTVType);
        customerQueue.add(customer);
        // call sorted data
        sortCustomerData(customerData);  
        // Display details of the TVs purchased
        System.out.println("\nCustomer " + name + " purchased the following TVs:");
        for(String tvId : purchasedTvIds) {
            System.out.println("The TV id number is: " + tvId);
        }
        
        // ask the customer if they want delivery
        System.out.print("Does the customer want delivery? (y/n): ");
        String deliveryResponse = scan.next().trim();

        if (deliveryResponse.equalsIgnoreCase("y")) {
            System.out.print("Enter the delivery address: ");
            scan.nextLine(); // Consume the leftover newline
            String deliveryAddress = scan.nextLine().trim();


			// Call method to append delivery info
            appendDeliveryInfo(name, deliveryAddress, accountNumber, numOfTvs, deliveryHeap);
            System.out.println("Current delivery heap size: " + deliveryHeap.getSize());

            
            

        }


        System.out.println("There are now " + inventory.size() + " TVs left in inventory.");
        
        if (accountNumber.equalsIgnoreCase("none") || existingCustomer == null) {
        	System.out.println("You've added a new customer. Please save the data.");
        	customerData.saveCustomerDataToFile(); // Save all customer data
        } else {
        	System.out.println("You've made an new order for an existing customer. Please save the data.");
        	customerData.saveCustomerDataToFile();  // Save data after updating the existing customer's order
        }
        return customerData;
        }        
    // function processes the checkout for the first customer in line and so on.
    private static void handleCustomerCheckout(Queue<Customer> customerQueue) {
    	// output for receipt
    	Customer customer = customerQueue.poll();
    	System.out.println(customer);
    	
    	// check if queue is occupied or empty
    	if(!customerQueue.isEmpty()) {
    		 System.out.println("\nThere are " + customerQueue.size() + " customers left in the queue.");
    	} 
    	else {
    	    System.out.println("There are no customers to checkout.");
    	}
    }
    // method handles customerUpdate menu
    private static void handleCustomerUpdateMenu(CustomerData customerData) {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        boolean dataChanged = false;
  
        do {
            System.out.println("\nCustomer Update Menu Options:");
            System.out.println("1 - Add a Customer");
            System.out.println("2 - Delete a Customer");
            System.out.println("3 - Change Customer Name");
            System.out.println("4 - Save Changes");
            System.out.println("5 - Display Customer List");
            System.out.println("6 - Return to Main");
            System.out.print("Please enter the menu choice: ");
            // try..catch block for validation
            try {
                choice = scan.nextInt();
                scan.nextLine();  // consume the newline

                switch(choice) {
                	// Add a Customer
                    case 1:  
                        System.out.print("Please enter customer's name: ");
                        String name = scan.nextLine();
                        System.out.print("Please enter the customer's account number: ");
                        String accountNumber = scan.nextLine();
                        // call method from customerData class
                        while (customerData.findCustomer(accountNumber) != null) {
                            System.out.println("Account number already exists. Please enter a new account number:");
                            accountNumber = scan.nextLine();
                        }
                        Customer newCustomer = new Customer(name, accountNumber);
                        // add customer
                        customerData.addCustomer(newCustomer);
                        System.out.println("Customer added successfully!");
                        dataChanged = true;
                        break;
                    // Delete a Customer
                    case 2: 
                        System.out.print("Please enter the customer's account number: ");
                        accountNumber = scan.nextLine();
                        // call method from customerData class
                        while (customerData.findCustomer(accountNumber) == null) {
                            System.out.println("Account number not found. Please reenter an existing account number:");
                            accountNumber = scan.nextLine();
                        }
                        // remove customer
                        customerData.removeCustomer(accountNumber);
                        System.out.println("Customer removed successfully!");
                        dataChanged = true;
                        break;
                    // Change Customer Name
                    case 3: 
                        System.out.print("Please enter the customer's account number: ");
                        accountNumber = scan.nextLine();
                        while (customerData.findCustomer(accountNumber) == null) {
                            System.out.println("Account number not found. Please reenter an existing account number:");
                            accountNumber = scan.nextLine();
                        }
                        System.out.print("Please enter the new name for this account number: ");
                        String newName = scan.nextLine();
                        // call method from customerData class
                        customerData.updateCustomerName(accountNumber, newName);
                        System.out.println("Name updated successfully!");
                        dataChanged = true;
                        break;
                    // Save Changes to a file
                    case 4:  
                    	// call method from customerData class
                    	customerData.saveCustomerDataToFile();
                        dataChanged = false;
                        break;
                    // display customer list
                    case 5: 
                    	customerData = sortCustomerData(customerData);
                        customerData.displayCustomer();
                        break;
                    // return to main menu
                    case 6:
                        // Check if there have been any changes to the data.
                        if (dataChanged) {
                            boolean validInput = false;
                            while (!validInput) {
                            	// Print out the customer list before exiting
                                customerData.displayCustomer();
                                // Prompt the user if they want to save changes before exiting.
                                System.out.println("You have unsaved changes. Do you want to save them? (Y/N)");
                                String saveChoice = scan.nextLine().trim().toUpperCase();

                                // If user chooses to save, execute the saving process.
                                if (saveChoice.equals("Y")) {
                                    // Save the customer data. 
                                    customerData.saveCustomerDataToFile();
                                    System.out.println("Changes saved successfully!");

                                    // The data is now saved, so reset the dataChanged flag
                                    dataChanged = false;

                                    // Since the user chose to save, set validInput to true, but do NOT exit from the while loop of the menu (so the menu is displayed again).
                                    validInput = true;  
                                } 
                                // If user chooses not to save, confirm and exit to the main menu.
                                else if (saveChoice.equals("N")) {
                                    validInput = true;  // Mark the input as valid to exit the loop.
                                    System.out.println("Returning to the main menu...");
                                    return; // Exit the method, returning to the main menu
                                } 
                                // If the user provides invalid input, inform and loop back for a new input.
                                else {
                                    System.out.println("Invalid input. Please enter Y or N.");
                                }
                            }
                        } else {
                            // If no changes were made, directly return to the main menu.
                            System.out.println("Returning to the main menu...");
                            
                        }
                        break;
                    // default is used as a switch statement, execute if none of the cases are entered
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch(Exception e) {
                System.out.println("Error: Invalid input. Please try again.");
                scan.nextLine();  // Clear the invalid input
            }  
        } while (choice != 6);
    }
    
    public static CustomerData sortCustomerData(CustomerData customerData) {
        // Convert CustomerData list to static Customer array
        Customer[] customerArray = customerDataToListArray(customerData);
        // Sort the array using merge sort
        mergeSort(customerArray, 0, customerArray.length - 1);
        // Create a temporary CustomerData list to hold the sorted data
        CustomerData sortedData = new CustomerData();
        for (Customer customer : customerArray) {
            sortedData.addCustomer(customer);
        }
        return sortedData;
    }

    // method to convert CustomerData list into a static array
    private static Customer[] customerDataToListArray(CustomerData data) {
        List<Customer> list = new ArrayList<>();
        // continue to iterate through list
        for (Customer customer : data) {
            list.add(customer);
        }
        return list.toArray(new Customer[0]);
    }
    // method for recursive sorting which orders arrays in ascending order
    // Recursive Insertion Sort method
    public static void insertionSortRecursive(Customer[] arr, int n) {
        // Base case
        if (n <= 1)
            return;

        // Insert the nth element at its correct position in sorted array.
        Customer last = arr[n - 1];
        int j = n - 2;

        // Move elements of arr[0..i-1] that are greater than key to one position ahead
        while (j >= 0 && arr[j].getAccountNumber().compareTo(last.getAccountNumber()) > 0) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = last;
    }

    // method to merge 
    private static void merge(Customer[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Create temp arrays
        Customer[] leftArray = new Customer[n1];
        Customer[] rightArray = new Customer[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            leftArray[i] = array[left + i];
        for (int j = 0; j < n2; ++j)
            rightArray[j] = array[middle + 1 + j];

        // Merge the temp arrays
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i].getAccountNumber().compareTo(rightArray[j].getAccountNumber()) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }
        // Copy remaining elements of leftArray[] if any
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy remaining elements of rightArray[] if any
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
    // sort the merging
    public static void mergeSort(Customer[] array, int left, int right) {
        if (left < right) {
            // Find the middle point
            int middle = (left + right) / 2;

            // Recursively sort the first and second halves
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            merge(array, left, middle, right);
        }
    }
 // Static method to read TV data and fill the Binary Tree
    public static BinaryTree readTVDataAndBuildTree() {
        Scanner scanner = new Scanner(System.in);
        String filePath;
        File tvFile;
        BinaryTree binaryTree = new BinaryTree();

        // Ask the user for the path of the tv.txt file and validate the file
        while (true) {
            System.out.print("Please enter the path of the tv.txt file: ");
            filePath = scanner.nextLine();
            tvFile = new File(filePath);
            if (tvFile.exists() && !tvFile.isDirectory()) {
                break;
            } else {
                System.out.println("Invalid file path. Please try again.");
            }
        }
        // Read the data from this file
        try (BufferedReader br = new BufferedReader(new FileReader(tvFile))) {
            String brand, model, priceLine;

            while ((brand = br.readLine()) != null &&
                   (model = br.readLine()) != null &&
                   (priceLine = br.readLine()) != null) {

                double price = Double.parseDouble(priceLine.trim()); // trim in case there are any leading/trailing spaces
                TVType tvType = new TVType(brand, model, price);

                // Insert the TVType object into the Binary Tree
                binaryTree.insert(tvType);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("An error occurred while parsing the price: " + e.getMessage());
        }

        // Return the Binary Tree to the calling method
        return binaryTree;
    }
    // method to display TV Options using binary Trees
    public static void displayTVOptions(BinaryTree tvTree) {
        System.out.println("TV Options:");
        System.out.println("Item  Brand      Model                 Cost");
        System.out.println("----  ----      -----                 ----");
        printTVTypesInOrder(tvTree.getRoot());
    }
    // Recursively prints the TV types stored in a binary tree in-order.
    public static void printTVTypesInOrder(Node node) {
        if (node != null) {
        	// Recursively traverse and print the left subtree
            printTVTypesInOrder(node.getLeft());
            
            // Access the TVType object directly from the Node
            TVType tv = node.getData();  
            //  TVType object
            System.out.println(tv);  
            // Recursively traverse and print the right subtree
            printTVTypesInOrder(node.getRight());
        }
    }
    //  Reads delivery information from a specified file and constructs a MaxHeap containing DelInfo objects with the data read from the file.
    public static MaxHeap readDeliveryInfoFile() {
    	// Initialize input components
        Scanner scanner = new Scanner(System.in);
        MaxHeap deliveryHeap = new MaxHeap(menuOptions.MAX_DELIVERY);
        BufferedReader reader = null;

        while (reader == null) {
            try {
                System.out.print("Please enter the delivery info file path: ");
                String filePath = scanner.nextLine();
                reader = new BufferedReader(new FileReader(filePath));
                System.out.println("Delivery Report");
            	System.out.println("----------------");

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim(); // Trim the line to remove leading and trailing white spaces
                    String[] parts = line.split(","); // Split the line by comma
                    if (parts.length == 4) {
                        // Trim each part to ensure white spaces don't cause issues
                        String customerName = parts[0].trim();
                        int accountNumber = Integer.parseInt(parts[1].trim());
                        String customerAddress = parts[2].trim();
                        int numberOfTVs = Integer.parseInt(parts[3].trim());
                        // Create a DelInfo object and insert it into the MaxHeap
                        DelInfo info = new DelInfo(customerName, accountNumber, customerAddress, numberOfTVs, deliveryHeap);
                        deliveryHeap.insert(info);
                    } else {
                        System.out.println(line);
                        
                        
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("The system cannot find the file specified. Please try again.");
            } catch (IOException e) {
                System.out.println("An I/O error occurred when reading the file.");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Error parsing integers from file.");
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Perform max heap transformation on the data
        deliveryHeap.maxHeap();
        // Return the constructed MaxHeap containing delivery information
        return deliveryHeap;
    }
    // static method to append data to the end of delivery information file
    public static void appendDeliveryInfo(String name, String address, String accountNumber, int numberOfTVs, MaxHeap deliveryHeap) {
        
    	// Ask the user for the filename and path
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the filename and path to append delivery information:");
        Path path = Paths.get(scanner.nextLine());
        
        

        // open in append mode
        try {
        	File myObj = new File(path.toString());
        	if (myObj.createNewFile()) {
        		System.out.println("File created: " + myObj.getName());
        	}
        	else {
        		System.out.println("File already exist");
        	}
        } catch(IOException e) {
        	System.out.println("An error occured.");
        	e.printStackTrace();
        }
        try {
        	FileWriter myWriter = new FileWriter(path.toString(), true);
        	// Calculate the delivery stop count based on the size of the heap
            int deliveryStopCount = deliveryHeap.getSize() + 1; 
        	myWriter.write("Delivery Stop Count: " + deliveryStopCount + "\n");
        	myWriter.write("Name: "+ name + "\tAccount Number: "+ accountNumber);
        	myWriter.write("\n");
        	myWriter.write("Address:" + address);
        	myWriter.write("\n");
        	myWriter.write("Number of TVs: " + numberOfTVs);
        	myWriter.write("\n");
        	myWriter.close();
        	System.out.println("Successfully wrote to the file");
    }catch(IOException e) {
    	e.printStackTrace();
    }
    }    
    // method to display the list in order and retrieve each object
    public static void displayDeliveryList() {
        // Call the method to read the delivery data file
        MaxHeap deliveries = readDeliveryInfoFile();
        
        if (deliveries == null) {
            System.out.println("Failed to read delivery information. Please check the file path and format.");
            return;
        }

        
        // Display the list in order, starting with the order with the most TVs
        while (deliveries.getSize() > 0) {
            // Retrieve and display each DelInfo object by calling remove root method
            DelInfo delivery = deliveries.remove();
            System.out.println(delivery.toString());
        }
    }





  
} 