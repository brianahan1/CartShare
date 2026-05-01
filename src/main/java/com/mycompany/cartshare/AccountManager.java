/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Server-side class, manages user accounts, balances, and charges. 
 */

package com.mycompany.cartshare;

import java.util.ArrayList;

public class AccountManager {
    private ArrayList<UserAccount> accounts = new ArrayList<>();

    public AccountManager() {
        accounts.add(new UserAccount("Matson", "pass1", 5000.00)); //Create existing accounts for testing
        accounts.add(new UserAccount("Smith", "pass2", 5000.00));
    }

    public boolean signUp(String username, String password, double balance) { //Signs up the user
        if (findAccount(username) != null) {
            return false;
        }

        accounts.add(new UserAccount(username, password, balance));
        return true;
    }

    public UserAccount login(String username, String password) { //checks username and password
        UserAccount account = findAccount(username);

        if (account != null && account.checkPassword(password)) {
            return account;
        }

        return null;
    }

    public boolean addCharge(String username, String chargeName, double amount) { //call add charge method
        UserAccount account = findAccount(username);

        if (account != null) {
            account.addCharge(new ChargeData(chargeName, amount));
            return true;
        }

        return false;
    }

    public ArrayList<ChargeData> getCharges(String username) { //grabs the active charges for an account
        UserAccount account = findAccount(username);

        if (account != null) {
            return account.getActiveCharges();
        }

        return new ArrayList<>();
    }

    public double payCharge(String username, int chargeIndex) { //pays an outstanding balance
        UserAccount account = findAccount(username);

        if (account != null) {
            account.payCharge(chargeIndex);
            return account.getBalance();
        }

        return -1;
    }

    public double getBalance(String username) { //grabs the user balance
        UserAccount account = findAccount(username);

        if (account != null) {
            return account.getBalance();
        }

        return -1;
    }

    public ArrayList<String> getUsernames() { //grabs available names for adding to groups
    ArrayList<String> usernames = new ArrayList<>();

    for (UserAccount account : accounts) {
        usernames.add(account.getUsername());
    }

    return usernames;
    }
    
    private UserAccount findAccount(String username) { //allows the program to check for existing accounts
        for (UserAccount account : accounts) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }

        return null;
    }
}