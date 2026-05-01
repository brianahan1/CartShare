/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Represents a single item. 
 */
package com.mycompany.cartshare;

public class ItemData extends AbstractRecord {
    public double price;

    public ItemData(String name, double price) {
        super(name);
        this.price = price;
    }
}