/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

import java.util.ArrayList;

public class ShoppingItem {
    private String item;
    private double price;
    private ArrayList<User> buyers;
    
    public String getItems() {
        return item;
    }
    
    public double getPrice () {
        return price;
    }
    
    public int numberOfBuyers () {
        return buyers.size();
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public void setPrice (double price) {
        this.price = price;
    }
    
    public String getName () {
        return this.item;
    }
}
