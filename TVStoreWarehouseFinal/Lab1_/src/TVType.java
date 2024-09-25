/**
 * @author Obinna Elemuo	
 * CMSY256 - Fall 2023
 * 11/2/2023
 * Represents a television type with attributes for brand, model, and price.
 * Provides access to these attributes through constructors, getters, and setters, 
 * and a formatted string output of the details.
 * @version 1.0
 */
public class TVType {
    // The brand of the TV.
    private String brand;

    // The model of the TV.
    private String model;

    // The price of the TV.
    private double price;

    // Default constructor.
    public TVType() {
    }

    // Constructor with all parameters.
    public TVType(String brand, String model, double price) {
        // Set the brand of the TV.
        this.brand = brand;

        // Set the model of the TV.
        this.model = model;

        // Set the price of the TV.
        this.price = price;
    }
    
    // getter and setters for brand, model, and price of TVS
    public String getBrand() {
       
        return brand;
    }

    public void setBrand(String brand) {
        
        this.brand = brand;
    }

    public String getModel() {
        
        return model;
    }

    
    public void setModel(String model) {
        
        this.model = model;
    }

    
    public double getPrice() {
        
        return price;
    }

    
    public void setPrice(double price) {
        
        this.price = price;
    }

    // Returns a string representation of the TV type.
    @Override
    public String toString() {
        
        return String.format("%-10s %-20s $%.2f", brand, model, price);
    }


}
