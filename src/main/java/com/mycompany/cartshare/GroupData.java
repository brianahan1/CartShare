/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Represents a single group. 
 */
package com.mycompany.cartshare;

import java.util.ArrayList;

public class GroupData extends AbstractRecord {
    public ArrayList<String> members = new ArrayList<>();
    public ArrayList<ItemData> items = new ArrayList<>();

    public GroupData(String name) {
        super(name);
    }
}