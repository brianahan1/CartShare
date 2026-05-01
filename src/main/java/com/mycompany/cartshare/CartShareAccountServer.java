/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Backend for CartShare. Waits for requests from CartShareClient.
 * Based on the chat app we made for one of the labs. 
 * Takes a string of commands separated by | and parses it to figure out what to do. 
 */
package com.mycompany.cartshare;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CartShareAccountServer {

    private AccountManager accountManager = new AccountManager();

    public void myServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("CartShare Account Server started on port 8189");

            while (true) {
                Socket incoming = serverSocket.accept();

                try {
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStream = incoming.getOutputStream();

                    Scanner in = new Scanner(inStream);
                    PrintWriter out = new PrintWriter(outStream, true);

                    boolean done = false;

                    while (!done && in.hasNextLine()) {
                        String lineIn = in.nextLine();
                        System.out.println("Server received: " + lineIn);

                        String[] parts = lineIn.split("\\|");

                        if (parts[0].equals("LOGIN") && parts.length == 3) {
                            String username = parts[1];
                            String password = parts[2];

                            UserAccount account = accountManager.login(username, password);

                            if (account != null) {
                                out.println("SUCCESS|" + account.getUsername() + "|" + account.getBalance());
                            } else {
                                out.println("FAIL");
                            }

                            done = true;

                        } else if (parts[0].equals("SIGNUP") && parts.length == 4) {
                            String username = parts[1];
                            String password = parts[2];
                            double balance = Double.parseDouble(parts[3]);

                            boolean success = accountManager.signUp(username, password, balance);

                            if (success) {
                                out.println("SIGNED_UP");
                            } else {
                                out.println("USER_EXISTS");
                            }

                            done = true;

                        } else if (parts[0].equals("ADDCHARGE") && parts.length == 4) {
                            String username = parts[1];
                            String chargeName = parts[2];
                            double amount = Double.parseDouble(parts[3]);

                            boolean success = accountManager.addCharge(username, chargeName, amount);

                            if (success) {
                                out.println("CHARGE_ADDED");
                            } else {
                                out.println("USER_NOT_FOUND");
                            }

                            done = true;

                        } else if (parts[0].equals("FETCHCHARGES") && parts.length == 2) {
                            String username = parts[1];

                            ArrayList<ChargeData> charges = accountManager.getCharges(username);

                            for (ChargeData charge : charges) {
                                out.println("CHARGE|" + charge.getName() + "|" + charge.amount);
                            }

                            out.println("END");
                            done = true;

                        } else if (parts[0].equals("PAYCHARGE") && parts.length == 3) {
                            String username = parts[1];
                            int chargeIndex = Integer.parseInt(parts[2]);

                            double newBalance = accountManager.payCharge(username, chargeIndex);

                            if (newBalance >= 0) {
                                out.println("BALANCE|" + newBalance);
                            } else {
                                out.println("FAIL");
                            }

                            done = true;

                        } else if (parts[0].equals("BALANCE") && parts.length == 2) {
                            String username = parts[1];

                            double balance = accountManager.getBalance(username);

                            if (balance >= 0) {
                                out.println("BALANCE|" + balance);
                            } else {
                                out.println("FAIL");
                            }

                            done = true;

                        } else if (parts[0].equals("LISTUSERS")) {
                            ArrayList<String> usernames = accountManager.getUsernames();

                            for (String username : usernames) {
                                out.println("USER|" + username);
                            }

                            out.println("END");
                            done = true;

                        } else if (lineIn.trim().equals("BYE")) {
                            out.println("closing server");
                            done = true;

                        } else {
                            out.println("ERROR");
                            done = true;
                        }
                    }

                } catch (Exception exc1) {
                    exc1.printStackTrace();
                } finally {
                    incoming.close();
                }
            }

        } catch (Exception exc2) {
            exc2.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CartShareAccountServer server = new CartShareAccountServer();
        server.myServer();
    }
}