/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Mediator between the GUI and the server. 
 * When the user does something in the GUI that requires the server, it calls a method from here. 
 * This class then sends a message to CartShareAccountServer.
 */
package com.mycompany.cartshare;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CartShareClient {

    private String sendRequest(String request) {
        try {
            Socket socket = new Socket("127.0.0.1", 8189);

            try {
                InputStream inStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);

                out.println(request);

                if (in.hasNextLine()) {
                    return in.nextLine();
                }

            } finally {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    //Methods for sending messages to the server. 
    public String loginClient(String username, String password) {
        return sendRequest("LOGIN|" + username + "|" + password);
    }

    public String signUpClient(String username, String password, double balance) {
        return sendRequest("SIGNUP|" + username + "|" + password + "|" + balance);
    }

    public String addChargeClient(String username, String chargeName, double amount) {
        return sendRequest("ADDCHARGE|" + username + "|" + chargeName + "|" + amount);
    }

    public String payChargeClient(String username, int chargeIndex) {
        return sendRequest("PAYCHARGE|" + username + "|" + chargeIndex);
    }

    public String balanceClient(String username) {
        return sendRequest("BALANCE|" + username);
    }

    public ArrayList<ChargeData> fetchChargesClient(String username) { //gets the list of outstanding charges for the user. 
        ArrayList<ChargeData> charges = new ArrayList<>();

        try {
            Socket socket = new Socket("127.0.0.1", 8189);

            try {
                InputStream inStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);

                out.println("FETCHCHARGES|" + username);

                while (in.hasNextLine()) {
                    String line = in.nextLine();

                    if (line.equals("END")) {
                        break;
                    }

                    String[] parts = line.split("\\|");

                    if (parts.length == 3 && parts[0].equals("CHARGE")) {
                        String chargeName = parts[1];
                        double amount = Double.parseDouble(parts[2]);

                        charges.add(new ChargeData(chargeName, amount));
                    }
                }

            } finally {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return charges;
    }

    public ArrayList<String> fetchUsersClient() { //grabs the list of users for adding them to groups
        ArrayList<String> usernames = new ArrayList<>();

        try {
            Socket socket = new Socket("127.0.0.1", 8189);

            try {
                InputStream inStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true);

                out.println("LISTUSERS");

                while (in.hasNextLine()) {
                    String line = in.nextLine();

                    if (line.equals("END")) {
                        break;
                    }

                    String[] parts = line.split("\\|");

                    if (parts.length == 2 && parts[0].equals("USER")) {
                        usernames.add(parts[1]);
                    }
                }

            } finally {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usernames;
    }
}