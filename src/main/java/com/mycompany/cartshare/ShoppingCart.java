/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

import java.util.ArrayList;

public class ShoppingCart {
    private int orderNo;
    private String retailer;
    private Group group;
    private User paidBy;
    private String date;
    private ArrayList<ShoppingItem> items;
    
    public ShoppingCart() {
        items = new ArrayList<>();
    }
    public double getTotalCost () {
        double TotalCost = 0;
        for (ShoppingItem item: items) {
            TotalCost += item.getPrice();
        }
        return TotalCost;
    }
        
    public void setRetailer (String retailer) {
        this.retailer = retailer;
    }
    public void setDate (String date) {
        this.date = date;
    }
    
    public void addItem (String name, double price) {
        ShoppingItem newItem = new ShoppingItem();
        newItem.setItem(name);
        newItem.setPrice(price);
        items.add(newItem);
    }
}
