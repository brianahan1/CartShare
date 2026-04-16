package com.mycompany.cartshare;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract class that defines the contract for splitting a bill among group members.
 * Subclasses implement specific split strategies (e.g., equal split, per-item split).
 */
public abstract class BillSplit {

    protected ShoppingCart cart;
    protected ArrayList<User> members;

    // Constructor
    public BillSplit(ShoppingCart cart, ArrayList<User> members) {
        this.cart = cart;
        this.members = members;
    }

    /**
     * Calculates how much each user owes.
     * Must be implemented by subclasses with a specific split strategy.
     * @return a map of each User to the amount they owe
     */
    public abstract HashMap<User, Double> calculateSplit();

    /**
     * Returns the total cost of all items in the cart.
     * Shared helper for all subclasses.
     * @return total price as a double
     */
    protected double calculateTotal() {
        double total = 0.0;
        for (ShoppingItem item : cart.getItems()) {
            try {
                total += Double.parseDouble(item.getPrice());
            } catch (NumberFormatException e) {
                System.out.println("Warning: Could not parse price for item: " + item.getItem());
            }
        }
        return total;
    }

    /**
     * Prints the split results to the console.
     * Uses calculateSplit() so subclasses automatically get correct output.
     */
    public void printSplit() {
        HashMap<User, Double> split = calculateSplit();
        System.out.println("=== Bill Split Summary ===");
        System.out.printf("Total: $%.2f%n", calculateTotal());
        System.out.println("--------------------------");
        for (HashMap.Entry<User, Double> entry : split.entrySet()) {
            User user = entry.getKey();
            double amount = entry.getValue();
            System.out.printf("%s %s owes: $%.2f%n",
                user.getFirstName(), user.getLastName(), amount);
        }
    }

    public ShoppingCart getCart() { return cart; }
    public ArrayList<User> getMembers() { return members; }
}

