/**
 * @author Obinna Elemuo	
 * CMSY256 Fall 2023
 * 12/7/2023
 * Customer.java - Interface
 * Defines constant values representing the
 * different actions a user can take within the TV Store Inventory. More Constants were added to be called for
 * the customer Update submenu. Constants will be called by main driver 
 * @version 1.0
 */
// interface to determine final variables, to be implemented by driver class
public interface menuOptions {
	// variables for main menu
	final int stockShelves = 1;
	final int fillWebOrder = 2;
	final int restockReturn = 3;
	final int restockInventory = 4;
	final int CUSTOMER_UPDATE = 5;
	final int CUSTOMER_PURCHASE = 6;
	final int CUSTOMER_CHECKOUT = 7;
	final int DISPLAY_DELIVERY_LIST = 8;
	final int DISPLAY_INVENTORY = 9;
	final int endProgram = 10;
	
	// variables for the Customer Data submenu
	final int ADD_CUSTOMER = 1;
	final int DELETE_CUSTOMER = 2;
	final int UPDATE_CUSTOMER_NAME = 3;
	final int SAVE_TO_FILE = 4;
	final int DISPLAY_CUSTOMER_LIST = 5;
	final int RETURN_TO_MAIN_MENU = 6;
	
	

	// cost for each TV including sales tax
	double TV_COST = 199.95;
	double SALES_TAX = 0.06;
	final int MAX_DELIVERY = 25;
}
