/**
 * @author Obinna Elemuo	
 * CMSY265 Fall 2023
 * 12/07/2023
 * TV.java - Controller Class
 * This class represents each Television and Television type in the TV Store inventory.
 * It will return established variables and set values for them. This class now utilizes the Comparable
 * @version 1.0
 */
public class TV implements Comparable<TV>{
	private String id; // represents 'ID number'
	private TVType tvType; // new instance variable of type TVType
	
	// default constructor to set string to null
	public TV() {
		this.id = null;
		this.tvType = null;
	}
	// constructor that sets a string to a value sent by parameter
	public TV(String id) {
		this.id = id;
		this.tvType = null;
	}
	
	// Getter for ID
	public String getID() {
		return id;
	}
	
	// Setter for ID
	public void setID(String id) {
		this.id = id;
	}

	 // Implementing compareTo method for Comparable interface
	@Override
    public int compareTo(TV other) {
        return this.id.compareTo(other.id);
    }

    // Updated toString to handle tvType
    @Override
    public String toString() {
        if (tvType == null) {
            return "The ID number is: " + id;
        } else {
            return "The ID number is: " + id + ", " + tvType.toString();
        }
    }
}
