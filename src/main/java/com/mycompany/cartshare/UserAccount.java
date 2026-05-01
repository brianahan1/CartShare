/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * represents an account.
 * Contains username, password, balance, and active charges
 */
package com.mycompany.cartshare;

import java.util.ArrayList;

public class UserAccount extends AbstractRecord {
    private String password;
    private double balance;
    private ArrayList<ChargeData> activeCharges = new ArrayList<>();

    public UserAccount(String username, String password, double balance) {
        super(username);
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() {
        return name;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public double getBalance() {
        return balance;
    }

    public void subtractFromBalance(double amount) {
        balance -= amount;
    }

    public void addCharge(ChargeData charge) {
        activeCharges.add(charge);
    }

    public ArrayList<ChargeData> getActiveCharges() {
        return activeCharges;
    }

    public ChargeData payCharge(int index) {
        if (index >= 0 && index < activeCharges.size()) {
            ChargeData charge = activeCharges.remove(index);
            subtractFromBalance(charge.amount);
            return charge;
        }

        return null;
    }
}