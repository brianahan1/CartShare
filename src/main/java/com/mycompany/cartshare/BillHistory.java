package com.mycompany.cartshare;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Records all transactions for a ShoppingGroup.
 * Serves as a history log of completed shopping carts and their splits.
 */
public class BillHistory {

    private String groupId;
    private ArrayList<ShoppingCart> transactionHistory;
    private LocalDateTime lastUpdated;

    // Constructor
    public BillHistory(String groupId) {
        this.groupId = groupId;
        this.transactionHistory = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Adds a completed ShoppingCart to the transaction history.
     * @param cart the completed cart to record
     */
    public void addTransaction(ShoppingCart cart) {
        transactionHistory.add(cart);
        lastUpdated = LocalDateTime.now();
    }

    /**
     * Retrieves all recorded transactions.
     * @return list of all ShoppingCarts in history
     */
    public ArrayList<ShoppingCart> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Returns the timestamp of the most recent update.
     * @return LocalDateTime of last update
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getGroupId() {
        return groupId;
    }

    /**
     * Prints a summary of all transactions to the console.
     */
    public void printHistory() {
        System.out.println("=== Bill History for Group: " + groupId + " ===");
        System.out.println("Last Updated: " + lastUpdated);
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            for (int i = 0; i < transactionHistory.size(); i++) {
                System.out.println("Transaction #" + (i + 1) + ": " + transactionHistory.get(i));
            }
        }
    }
}

