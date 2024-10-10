/**
 * @author Obinna Elemuo
 * CMSY265 - Fall 2023
 * 12/13/2023
 * This class represents the brand information for the cellphone sales.
 * It stores the number of units sold and the total sales amount for a brand.
 */
public class BrandSalesData {
    private int count;
    private double totalSales;

    // adds a sale to this brand. increments unit sold count and adds given price to totalSales
    public void addSale(double price) {
        count++;
        totalSales += price;
    }

    // gets the total number of units sold
    public int getCount() {
        return count;
    }

    // gets the total sales amount for this brand.
    public double getTotalSales() {
        return totalSales;
    }
}
