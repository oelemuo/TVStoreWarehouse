import java.util.ArrayList;
import java.io.Serializable;
/**
 * @author Obinna Elemuo	
 * CMSY265 Fall 2023
 * 12/07/2023
 * Customer.java - Controller Class
 * This class represents a customer in the TV Store system. It manages customer's invoice information
 * such as account number, number of TV's purchased, total cost of TV's, and an ArrayList holding the ID 
 * number of the TV's which are purchased. It also includes new instance variables to set type to null in order to implement Binary Tree 
 * With Lab 3 updates, additional functionalities were introduced 
 * to better manage customer transactions and interactions within the store. This class is called in the driver class.
 * @version 1.0
 */
public class Customer implements Serializable {
	// Private instance variables to store details for each customer invoice.
	private String customerName;
	private String accountNumber;
	private int numOfTvsPurchased;
	private double costOfTVs;
	private ArrayList<String> tvIdPurchased;
	private TVType tvType; 
	
	// Default constructor
	public Customer(String customerName, String accountNumber) {
	    this.customerName = customerName;
	    this.accountNumber = accountNumber;
	    this.numOfTvsPurchased = 0;
	    this.tvIdPurchased = new ArrayList<>();
	    this.costOfTVs = 0.0;
	    this.tvType = null; 
	}

	// Parameterized constructor
	public Customer(String customerName, String accountNumber, int numOfTvsPurchased, ArrayList<String> tvIdPurchased, TVType tvType) {
	    this.customerName = customerName;
	    this.accountNumber = accountNumber;
	    this.numOfTvsPurchased = numOfTvsPurchased;
	    this.tvIdPurchased = tvIdPurchased;
	    this.tvType = tvType;
	    this.costOfTVs = calculateAmountDue(); // Call after tvType is set
	}

	// Getter for customerName
	public String getCustomerName() {
	    return customerName;
	}

	// Setter for customerName
	public void setCustomerName(String customerName) {
	    this.customerName = customerName;
	}

	// Getter for accountNumber
	public String getAccountNumber() {
	    return accountNumber;
	}

	// Setter for accountNumber
	public void setAccountNumber(String accountNumber) {
	    this.accountNumber = accountNumber;
	}

	// Getter for numOfTvsPurchased
	public int getNumOfTvsPurchased() {
	    return numOfTvsPurchased;
	}

	// Setter for numOfTvsPurchased
	public void setNumOfTvsPurchased(int numOfTvsPurchased) {
	    this.numOfTvsPurchased = numOfTvsPurchased;
	    this.costOfTVs = calculateAmountDue(); // update the total cost when the number of TVs is changed
	}

	// Getter for costOfTvs
	public double getCostOfTvs() {
	    return costOfTVs;
	}

	// NOTE: No setter for costOfTvs because it's calculated based on numOfTvsPurchased

	// Getter for tvIdsPurchased
	public ArrayList<String> getTvIdPurchased() {
	    return tvIdPurchased;
	}

	// Setter for tvIdsPurchased
	public void setTvIdsPurchased(ArrayList<String> tvIdPurchased) {
	    this.tvIdPurchased = tvIdPurchased;
	}
	// Getter for TVType
    public TVType getTVType() {
        return tvType;
    }

    // Setter for TVType
    public void setTVType(TVType tvType) {
        this.tvType = tvType;
        this.costOfTVs = calculateAmountDue(); // Update the total cost when TVType is changed
    }
	// Method to calculate the total amount due at purchase.
    public double calculateAmountDue() {
    	if (tvType != null) {
            // Calculate and return total cost before tax using price from TVType
            double totalCost = numOfTvsPurchased * tvType.getPrice();
            return totalCost + totalCost * menuOptions.SALES_TAX;
        } else {
            return 0.0; // Return 0 if TVType is not set
        }
    }
 
    // toString method for displaying the sales receipt
    @Override
    public String toString() {
    	// construct a receipt using stringBuilder
        StringBuilder receipt = new StringBuilder();
        receipt.append("\nCheckout Receipt:\n");
        receipt.append("Customer: ").append(customerName).append("\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        receipt.append("Purchased ").append(numOfTvsPurchased).append(" TVs for $").append(String.format("%.2f", costOfTVs)).append("\n");
        
        // iterate over the "tvIdPurchased' ArrayList. This will append each TV ID to the string
        for(String tvId : tvIdPurchased) {
            receipt.append("TV ID Purchased is: ").append(tvId).append("\n");
        }
        // return receipt
        return receipt.toString();
    }


}
