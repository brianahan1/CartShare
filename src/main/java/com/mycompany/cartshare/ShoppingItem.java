/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

import java.util.ArrayList;

public class ShoppingItem {
    private String item;
    private String price;
    private ArrayList<User> buyers;
    
    public String getName() {
        return item;
    }
    
    public String getPrice () {
        return price;
    }
}
