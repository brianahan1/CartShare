/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

import java.util.ArrayList;

public class Group {
    private String groupId;
    private String groupName;
    private ArrayList<User> members;
    
    public String getGroupName() {
        return this.groupName;
    }
    public ArrayList<User> getMembers() {
        return members;
    }
}
