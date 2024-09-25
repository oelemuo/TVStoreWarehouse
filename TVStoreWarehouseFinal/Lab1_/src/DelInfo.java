/**
 * @author Obinna Elemuo	
 * CMSY265 - Fall 2023
 * 12/07/2023
 * Class handles the delivery credentials for each customer. Contains instance variables
 * for customer account number, customer address and the number of TVs purchased.
 * @version 1.0
 */
public class DelInfo {
    private String customerName;
    private int accountNumber;
    private String customerAddress;
    private int numberOfTvs;
    private MaxHeap deliveryStopCount;

    // Default constructor
    public DelInfo() {
        // Initialize with default values if needed
    }
    // Full parameter constructor
    public DelInfo(String customerName, int accountNumber, String customerAddress, int numberOfTvs, MaxHeap deliveryHeap) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.customerAddress = customerAddress;
        this.numberOfTvs = numberOfTvs;
        this.deliveryStopCount = deliveryHeap;
    }
    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String delCustomerName) {
        this.customerName = delCustomerName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int delAccountNumber) {
        this.accountNumber = delAccountNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getNumberOfTvs() {
        return numberOfTvs;
    }

    public void setNumberOfTvs(int delNumberOfTvs) {
        this.numberOfTvs = delNumberOfTvs;
    }
    
    public MaxHeap getDeliveryStopCount() {
    	return deliveryStopCount;
    }
    
    public void setDeliveryStopCount(int deliveryStopCount) {
    	this.setDeliveryStopCount(deliveryStopCount);
    }

    // toString method
    @Override
    public String toString() {
        return "Customer Name: " + customerName +
               ", Account Number: " + accountNumber +
               ", Address: " + customerAddress +
               ", Number of TVs: " + numberOfTvs;
    }
}

