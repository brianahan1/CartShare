
package com.mycompany.cartshare;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private double currentBalance;
    private ArrayList<Group> groups; // List all the groups the user belongs to
    private ArrayList<User> friendList;
    
    public String getFirstName () {
        return firstName;
    }
    
    public String getLastName () {
        return lastName;
    }
    
    public double getCurrentBalance () {
        return currentBalance;
    }
}
