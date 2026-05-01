/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Stores a shared name field for data classes since most of them need a name
 */
package com.mycompany.cartshare;

public abstract class AbstractRecord {
    protected String name;

    public AbstractRecord(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}