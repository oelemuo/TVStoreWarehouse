import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
/**
 * @author Obinna Elemuo	
 * CMSY265 - Fall 2023
 * 12/07/2023
 * CustomerData.java - Data Management Class.
 * This class holds a list of customers for the TV Store and provides 
 * methods to manage this data such as adding, removing, updating, and searching for customers.
 * For the purpose of sorting operations, the class converts the customer data list
 * into a static array of type 'Customer'. Recursive method calls for sorting are 
 * most efficient when operating on a static array structure.
 * Additionally, the class offers an iterator method to facilitate sequential access to the customer data.
 * The main driver class will utilize these methods for handling customer-related operations.
 * @version 1.0
 */

public class CustomerData implements Iterable<Customer>, Serializable{
    private List<Customer> customerList;

    public CustomerData() {
        this.customerList = new LinkedList<>();
    }

    // Returns an iterator for the customer list
    public Iterator<Customer> iterator(){
        return customerList.iterator();
    }

    // Adds a customer to the customer list
    public CustomerData addCustomer(Customer customer) {
        this.customerList.add(customer);
        
        return this;
      
    }

    // Removes a customer with the given account number from the list
    public void removeCustomer(String accountNumber) {
        Customer removeCust = findCustomer(accountNumber);
        if(removeCust != null) {
            customerList.remove(removeCust);
        }
    }

    // Finds and returns a customer with the given account number, or null if not found
    public Customer findCustomer(String accountNumber) {
        for(Customer customer : customerList) {
            if(customer.getAccountNumber().equals(accountNumber)) {
                return customer;
            }
        }
        return null;
    }
   
    // Saves the customer data to a user-specified file using try-with-resources.
    public void saveCustomerDataToFile() {
        Scanner scanner = new Scanner(System.in);  // Scanner object to read user input
        boolean savedSuccessfully = false;

        while (!savedSuccessfully) {
            // Prompt the user for a filename
            System.out.print("Please enter the name of the file to save: ");
            String fileName = scanner.nextLine();

            // Check if file already exists
            if (Files.exists(Paths.get(fileName))) {
                System.out.println("File already exists. Please choose a different name or ensure you want to overwrite.");
                System.out.print("Do you want to overwrite? (yes/no): ");
                String decision = scanner.nextLine().trim().toLowerCase();
                if (!"yes".equals(decision)) {
                    continue;
                }
            }
            // Using BufferedWriter automatic resource management
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {

                // Loop through each customer in the customer list
                for (Customer customer : customerList) {
                    // Write each customer's data to the file
                    bufferedWriter.write(customer.getCustomerName());
                    bufferedWriter.newLine();  // New line
                    bufferedWriter.write(customer.getAccountNumber());
                    bufferedWriter.newLine();  // New line
                }
                // Confirm file has been saved
                System.out.println("File created: " + fileName);
                System.out.println("Successfully wrote to the file");
                savedSuccessfully = true; // The file has been saved successfully, exit the loop

            } catch (IOException e) {
                System.out.println("Error writing to the file: " + e.getMessage());
                System.out.println("Please try again with a valid filepath.");
            }
        }
    }
    // Updates the name of a customer with the given account number
    public void updateCustomerName(String accountNumber, String newName) {
        Customer customerToUpdate = findCustomer(accountNumber);
        if(customerToUpdate != null) {
            customerToUpdate.setCustomerName(newName);
        }
    }
    
    // Method to convert the customer data list into a Customer array
    public Customer[] convertListToArray() {
        // Convert the customerList to an Object array
        Object[] objectArray = customerList.toArray();

        // Create a Customer array of the same size using length
        Customer[] customerArray = new Customer[objectArray.length];

        // Loop through the object array and cast each object to a Customer
        // Set the appropriate index in the Customer array to the casted value from the Object array
        for (int i = 0; i < objectArray.length; i++) {
            customerArray[i] = (Customer) objectArray[i];
        }
        return customerArray;
    }

    // Returns the number of customers in the list
    public int getCustomerCount() {
        return customerList.size();
    }

 // Recursive Insertion Sort Helper function.
    private void recursiveInsertionSort(Customer[] arr, int n) {
        // Base case
        if (n <= 1)
            return;

        // Sort first n-1 elements
        recursiveInsertionSort(arr, n - 1);

        // Insert the nth element at its correct position in the sorted array
        Customer last = arr[n - 1];
        int j = n - 2;

        while (j >= 0 && arr[j].getAccountNumber().compareTo(last.getAccountNumber()) > 0) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = last;
    }
    
    // Displays the names of all customers in the list
    public void displayCustomer() {
    	// Convert the list to a static Customer array.
        Customer[] customers = convertListToArray();
        // Sort the customers recursively.
        recursiveInsertionSort(customers, customers.length);
           	
        System.out.println("Customer List:");
        System.out.println("--------------");
        if (customers.length == 0) {
        	System.out.println("No customers available.");
        }
        
        int counter = 1;
        for (Customer customer : customers) {
            System.out.printf("Customer %d: %-20s Account Number: %s%n", counter, customer.getCustomerName(), customer.getAccountNumber());
            counter++;
        }
    }

}

