/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * represents currency selected by the user. 
 * stores currency name, symbol, and rate. 
 */
package com.mycompany.cartshare;

public class CurrencyData extends AbstractRecord {
    private String symbol;
    private double rate;

    public CurrencyData(String currencyName, String symbol, double rate) {
        super(currencyName);
        this.symbol = symbol;
        this.rate = rate;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getRate() {
        return rate;
    }
}