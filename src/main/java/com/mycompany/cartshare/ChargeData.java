/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * represents a single owed charge
 * used by the ledger panel, and by the server (when the group cost is split)
 */
package com.mycompany.cartshare;

public class ChargeData extends AbstractRecord {
    public double amount;

    public ChargeData(String name, double amount) {
        super(name);
        this.amount = amount;
    }
}