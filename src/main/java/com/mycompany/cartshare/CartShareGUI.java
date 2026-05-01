/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Front end of CartShare. uses the ActionListener interface for buttons.
 * The GUI calls on methods from CartShareClient to communicate back and fourth from the server
 */
package com.mycompany.cartshare;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CartShareGUI extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JButton homeBtn;
    private JButton ledgerBtn;
    private JButton groupsBtn;
    private JButton accountBtn;
    private JButton notificationsBtn;
    private JButton logoutBtn;

    private JLabel usernameLabel;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;

    private String currentUser = "";
    private double currentBalance = 0.0;

    private CurrencyData currentCurrency = new CurrencyData("USD", "$", 1.0);

    private CartShareClient client = new CartShareClient();
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    private ArrayList<GroupData> groups = new ArrayList<>();
    private ArrayList<ChargeData> activeCharges = new ArrayList<>();

    private DefaultListModel<String> groupListModel = new DefaultListModel<>();
    private DefaultListModel<String> memberListModel = new DefaultListModel<>();
    private DefaultListModel<String> itemListModel = new DefaultListModel<>();
    private DefaultListModel<String> activeChargesModel = new DefaultListModel<>();
    private DefaultListModel<String> paidChargesModel = new DefaultListModel<>();
    private DefaultListModel<String> notificationsModel = new DefaultListModel<>();

    private JComboBox<String> memberBox; //this was originally a textfield, but comboboxes work better. 
    private JList<String> groupList;

    public CartShareGUI() {
        setTitle("CartShare");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createTopBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createAccountPanel(), "ACCOUNT");
        mainPanel.add(createHomePanel(), "HOME");
        mainPanel.add(createGroupsPanel(), "GROUPS");
        mainPanel.add(createLedgerPanel(), "LEDGER");
        mainPanel.add(createNotificationsPanel(), "NOTIFICATIONS");

        add(mainPanel, BorderLayout.CENTER);

        logoutUser();

        setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.BLACK);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("CartShare");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        topBar.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);

        homeBtn = createBlueButton("HOME");
        ledgerBtn = createBlueButton("LEDGER");
        groupsBtn = createBlueButton("GROUPS");
        accountBtn = createBlueButton("ACCOUNT");
        notificationsBtn = createBlueButton("NOTIFICATIONS");

        buttonPanel.add(homeBtn);
        buttonPanel.add(ledgerBtn);
        buttonPanel.add(groupsBtn);
        buttonPanel.add(accountBtn);
        buttonPanel.add(notificationsBtn);

        topBar.add(buttonPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout());
        rightPanel.setBackground(Color.BLACK);

        usernameLabel = new JLabel("");
        usernameLabel.setForeground(Color.WHITE);

        logoutBtn = createBlueButton("Logout");

        rightPanel.add(usernameLabel);
        rightPanel.add(logoutBtn);

        topBar.add(rightPanel, BorderLayout.EAST);

        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));

        ledgerBtn.addActionListener(e -> {
            refreshActiveCharges();
            cardLayout.show(mainPanel, "LEDGER");
        });

        groupsBtn.addActionListener(e -> {
            refreshUserComboBox();
            cardLayout.show(mainPanel, "GROUPS");
        });

        accountBtn.addActionListener(e -> cardLayout.show(mainPanel, "ACCOUNT"));
        notificationsBtn.addActionListener(e -> cardLayout.show(mainPanel, "NOTIFICATIONS"));
        logoutBtn.addActionListener(e -> logoutUser());

        return topBar;
    }

    private JButton createBlueButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void setButtonsActive(boolean active) {
        homeBtn.setEnabled(active);
        ledgerBtn.setEnabled(active);
        groupsBtn.setEnabled(active);
        notificationsBtn.setEnabled(active);
        logoutBtn.setEnabled(active);
        accountBtn.setEnabled(true);
    }

    private void logoutUser() {
        currentUser = "";
        currentBalance = 0.0;

        usernameLabel.setText("");
        activeCharges.clear();
        activeChargesModel.clear();

        updateHomeLabels();
        setButtonsActive(false);

        if (cardLayout != null && mainPanel != null) {
            cardLayout.show(mainPanel, "ACCOUNT");
        }
    }

    private void updateHomeLabels() { //important method for refreshing displayed data
        if (welcomeLabel != null && balanceLabel != null) {
            welcomeLabel.setText("Welcome, " + currentUser);
            balanceLabel.setText("Current Balance: " + formatMoney(currentBalance));
        }
    }

    private void setCurrency(String currencyName) { //lets user set the currency
        String apiCurrencyCode = currencyName;
        String symbol = "$";

        if (currencyName.equals("USD")) {
            apiCurrencyCode = "USD";
            symbol = "$";
        } else if (currencyName.equals("GBP")) {
            apiCurrencyCode = "GBP";
            symbol = "£";
        } else if (currencyName.equals("EURO")) {
            apiCurrencyCode = "EUR";
            symbol = "€";
        }

        double rate = exchangeRateService.getRate(apiCurrencyCode);

        currentCurrency = new CurrencyData(currencyName, symbol, rate);

        updateHomeLabels();
        refreshActiveCharges();
        loadCurrentlySelectedGroupAgain();
    }

    private String formatMoney(double usdAmount) {
        double convertedAmount = usdAmount * currentCurrency.getRate();
        return currentCurrency.getSymbol() + String.format("%.2f", convertedAmount);
    }

    private void addNotification(String message) {
        NotificationData notification = new NotificationData(message);
        notificationsModel.addElement(notification.getFormattedNotification());
    }

    private void refreshUserComboBox() {
        if (memberBox == null) {
            return;
        }

        memberBox.removeAllItems();

        ArrayList<String> usernames = client.fetchUsersClient();

        for (String username : usernames) {
            memberBox.addItem(username);
        }
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel signUpPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        signUpPanel.setBackground(Color.DARK_GRAY);
        signUpPanel.setBorder(BorderFactory.createTitledBorder("SIGN UP"));

        JTextField signUpUsername = new JTextField();
        JPasswordField signUpPassword = new JPasswordField();
        JTextField startingBalance = new JTextField();
        JButton signUpButton = createBlueButton("sign up");

        signUpPanel.add(createLabel("Username:"));
        signUpPanel.add(signUpUsername);
        signUpPanel.add(createLabel("Password:"));
        signUpPanel.add(signUpPassword);
        signUpPanel.add(createLabel("Starting Balance:"));
        signUpPanel.add(startingBalance);
        signUpPanel.add(signUpButton);

        JPanel loginPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        loginPanel.setBackground(Color.DARK_GRAY);
        loginPanel.setBorder(BorderFactory.createTitledBorder("LOGIN"));

        JTextField loginUsername = new JTextField();
        JPasswordField loginPassword = new JPasswordField();
        JComboBox<String> currencyBox = new JComboBox<>(new String[]{"USD", "GBP", "EURO"});
        JButton loginButton = createBlueButton("log in");

        loginPanel.add(createLabel("Username:"));
        loginPanel.add(loginUsername);
        loginPanel.add(createLabel("Password:"));
        loginPanel.add(loginPassword);
        loginPanel.add(createLabel("Currency:"));
        loginPanel.add(currencyBox);
        loginPanel.add(loginButton);

        signUpButton.addActionListener(e -> {
            try {
                String username = signUpUsername.getText();
                String password = new String(signUpPassword.getPassword());
                double balance = Double.parseDouble(startingBalance.getText());

                String result = client.signUpClient(username, password, balance);

                if (result != null && result.equals("SIGNED_UP")) {
                    addNotification("Account created for " + username);

                    signUpUsername.setText("");
                    signUpPassword.setText("");
                    startingBalance.setText("");

                    refreshUserComboBox();

                    JOptionPane.showMessageDialog(this, "User signed up successfully!");
                } else if (result != null && result.equals("USER_EXISTS")) {
                    JOptionPane.showMessageDialog(this, "That username already exists.");
                } else {
                    JOptionPane.showMessageDialog(this, "Could not connect to server.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid sign up information.");
            }
        });

        loginButton.addActionListener(e -> {
            String username = loginUsername.getText();
            String password = new String(loginPassword.getPassword());

            String result = client.loginClient(username, password);

            if (result != null && result.startsWith("SUCCESS")) {
                String[] parts = result.split("\\|");

                currentUser = parts[1];
                currentBalance = Double.parseDouble(parts[2]);

                setCurrency((String) currencyBox.getSelectedItem());

                usernameLabel.setText(currentUser);
                updateHomeLabels();
                setButtonsActive(true);
                refreshActiveCharges();
                refreshUserComboBox();

                loginUsername.setText("");
                loginPassword.setText("");

                addNotification(currentUser + " logged in");
                addNotification("Currency set to " + currencyBox.getSelectedItem());

                cardLayout.show(mainPanel, "HOME");

            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.");
            }
        });

        panel.add(signUpPanel);
        panel.add(loginPanel);

        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        welcomeLabel = createLabel("Welcome, ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        balanceLabel = createLabel("Current Balance: $0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JList<String> activeGroups = new JList<>(groupListModel);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setBackground(Color.BLACK);
        top.add(welcomeLabel);
        top.add(balanceLabel);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(activeGroups), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGroupsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        groupList = new JList<>(groupListModel);
        JList<String> memberList = new JList<>(memberListModel);
        JList<String> itemList = new JList<>(itemListModel);

        JTextField groupNameField = new JTextField();
        memberBox = new JComboBox<>();
        JTextField itemNameField = new JTextField();
        JTextField itemPriceField = new JTextField();

        JButton createGroupBtn = createBlueButton("Create Group");
        JButton addMemberBtn = createBlueButton("Add Member");
        JButton addItemBtn = createBlueButton("Add Item");
        JButton uploadPhotoBtn = createBlueButton("Upload Photo");
        JButton calculateBtn = createBlueButton("Calculate Split");

        JButton deleteGroupBtn = createBlueButton("Delete Selected Group");
        JButton deleteMemberBtn = createBlueButton("Delete Selected Member");
        JButton deleteItemBtn = createBlueButton("Delete Selected Item");

        refreshUserComboBox();

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        inputPanel.setBackground(Color.BLACK);

        JPanel groupRow = new JPanel(new GridLayout(1, 3, 10, 10));
        groupRow.setBackground(Color.BLACK);
        groupRow.add(createLabel("Group Name:"));
        groupRow.add(groupNameField);
        groupRow.add(createGroupBtn);

        JPanel memberRow = new JPanel(new GridLayout(1, 3, 10, 10));
        memberRow.setBackground(Color.BLACK);
        memberRow.add(createLabel("Member Name:"));
        memberRow.add(memberBox);
        memberRow.add(addMemberBtn);

        JPanel itemRow1 = new JPanel(new GridLayout(1, 4, 10, 10));
        itemRow1.setBackground(Color.BLACK);
        itemRow1.add(createLabel("Item Name:"));
        itemRow1.add(itemNameField);
        itemRow1.add(createLabel("Item Price:"));
        itemRow1.add(itemPriceField);

        JPanel itemRow2 = new JPanel(new GridLayout(2, 2, 10, 10));
        itemRow2.setBackground(Color.BLACK);
        itemRow2.add(addItemBtn);
        itemRow2.add(calculateBtn);
        itemRow2.add(uploadPhotoBtn);

        inputPanel.add(groupRow);
        inputPanel.add(memberRow);
        inputPanel.add(itemRow1);
        inputPanel.add(itemRow2);

        JPanel listPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        listPanel.setBackground(Color.BLACK);

        listPanel.add(createListSection("Groups", groupList, deleteGroupBtn));
        listPanel.add(createListSection("Members", memberList, deleteMemberBtn));
        listPanel.add(createListSection("Items", itemList, deleteItemBtn));

        createGroupBtn.addActionListener(e -> {
            String groupName = groupNameField.getText();

            if (!groupName.isEmpty()) {
                groups.add(new GroupData(groupName));
                groupListModel.addElement(groupName);

                addNotification("Group created: " + groupName);
                groupNameField.setText("");
            }
        });

        groupList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedGroup(groupList.getSelectedIndex());
            }
        });

        addMemberBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();
            String member = (String) memberBox.getSelectedItem();

            if (index >= 0 && member != null && !member.isEmpty()) {
                if (!groups.get(index).members.contains(member)) {
                    groups.get(index).members.add(member);
                    memberListModel.addElement(member);

                    addNotification(member + " added to " + groups.get(index).getName());
                } else {
                    JOptionPane.showMessageDialog(this, "That user is already in this group.");
                }
            }
        });

        addItemBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();
            String item = itemNameField.getText();
            String price = itemPriceField.getText();

            if (index >= 0 && !item.isEmpty() && !price.isEmpty()) {
                try {
                    double priceAmount = Double.parseDouble(price);
                    ItemData newItem = new ItemData(item, priceAmount);

                    groups.get(index).items.add(newItem);
                    itemListModel.addElement(newItem.getName() + " - " + formatMoney(newItem.price));

                    addNotification("Item added to " + groups.get(index).getName() + ": " + newItem.getName());

                    itemNameField.setText("");
                    itemPriceField.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid item price.");
                }
            }
        });

        uploadPhotoBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();

            if (index < 0) {
                JOptionPane.showMessageDialog(this, "Please select a group first.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getName();

                ItemData scannedReceipt = new ItemData("scanned receipt", 53.00);
                groups.get(index).items.add(scannedReceipt);
                itemListModel.addElement(scannedReceipt.getName() + " - " + formatMoney(scannedReceipt.price));

                addNotification("Photo uploaded: " + fileName);
                addNotification("Scanned receipt added to " + groups.get(index).getName() + ": " + formatMoney(53.00));

                JOptionPane.showMessageDialog(this,
                        "Photo selected: " + fileName
                                + "\nAdded item: scanned receipt - " + formatMoney(53.00));
            }
        });

        calculateBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();

            if (index < 0) {
                JOptionPane.showMessageDialog(this, "Please select a group first.");
                return;
            }

            GroupData group = groups.get(index);
            double total = 0;

            for (ItemData item : group.items) {
                total += item.price;
            }

            int memberCount = group.members.size();

            if (memberCount == 0) {
                JOptionPane.showMessageDialog(this, "Add at least one member first.");
                return;
            }

            double split = total / memberCount;

            int successfulCharges = 0;
            int failedCharges = 0;

            for (String member : group.members) {
                String result = client.addChargeClient(
                        member,
                        group.getName() + " charge",
                        split
                );

                if (result != null && result.equals("CHARGE_ADDED")) {
                    successfulCharges++;
                } else {
                    failedCharges++;
                }
            }

            refreshActiveCharges();

            addNotification("Group split created for " + group.getName()
                    + ". Sent charges to " + successfulCharges + " users.");

            JOptionPane.showMessageDialog(this,
                    "Total: " + formatMoney(total)
                            + "\nMembers: " + memberCount
                            + "\nEach person owes: " + formatMoney(split)
                            + "\nCharges sent to " + successfulCharges + " users."
                            + "\nFailed charges: " + failedCharges);
        });

        deleteGroupBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();

            if (index >= 0) {
                addNotification("Group deleted: " + groups.get(index).getName());

                groups.remove(index);
                groupListModel.remove(index);
                memberListModel.clear();
                itemListModel.clear();
            }
        });

        deleteMemberBtn.addActionListener(e -> {
            int groupIndex = groupList.getSelectedIndex();
            int memberIndex = memberList.getSelectedIndex();

            if (groupIndex >= 0 && memberIndex >= 0) {
                String removedMember = groups.get(groupIndex).members.get(memberIndex);

                groups.get(groupIndex).members.remove(memberIndex);
                memberListModel.remove(memberIndex);

                addNotification("Member removed: " + removedMember);
            }
        });

        deleteItemBtn.addActionListener(e -> {
            int groupIndex = groupList.getSelectedIndex();
            int itemIndex = itemList.getSelectedIndex();

            if (groupIndex >= 0 && itemIndex >= 0) {
                String removedItem = groups.get(groupIndex).items.get(itemIndex).getName();

                groups.get(groupIndex).items.remove(itemIndex);
                itemListModel.remove(itemIndex);

                addNotification("Item removed: " + removedItem);
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createListSection(String title, JList<String> list, JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        panel.add(createLabel(title), BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        return panel;
    }

    private void loadSelectedGroup(int index) {
        memberListModel.clear();
        itemListModel.clear();

        if (index >= 0) {
            GroupData group = groups.get(index);

            for (String member : group.members) {
                memberListModel.addElement(member);
            }

            for (ItemData item : group.items) {
                itemListModel.addElement(item.getName() + " - " + formatMoney(item.price));
            }
        }
    }

    private void loadCurrentlySelectedGroupAgain() {
        if (groupList == null) {
            return;
        }

        int index = groupList.getSelectedIndex();
        loadSelectedGroup(index);
    }

    private void refreshActiveCharges() {
        activeCharges.clear();
        activeChargesModel.clear();

        if (currentUser.equals("")) {
            return;
        }

        ArrayList<ChargeData> chargesFromServer = client.fetchChargesClient(currentUser);

        for (ChargeData charge : chargesFromServer) {
            activeCharges.add(charge);
            activeChargesModel.addElement(charge.getName() + ": " + formatMoney(charge.amount));
        }
    }

    private JPanel createLedgerPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JList<String> activeChargesList = new JList<>(activeChargesModel);
        JList<String> paidChargesList = new JList<>(paidChargesModel);

        JButton payButton = createBlueButton("Pay Selected Charge");

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.add(createLabel("Active Charges"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(activeChargesList), BorderLayout.CENTER);
        leftPanel.add(payButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        rightPanel.add(createLabel("Paid Charges"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(paidChargesList), BorderLayout.CENTER);

        payButton.addActionListener(e -> {
            int index = activeChargesList.getSelectedIndex();

            if (index >= 0) {
                ChargeData charge = activeCharges.get(index);

                String result = client.payChargeClient(currentUser, index);

                if (result != null && result.startsWith("BALANCE")) {
                    String[] parts = result.split("\\|");
                    currentBalance = Double.parseDouble(parts[1]);

                    updateHomeLabels();

                    paidChargesModel.addElement(charge.getName() + ": " + formatMoney(charge.amount));

                    refreshActiveCharges();

                    addNotification("Charge paid: " + charge.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Could not pay charge. Make sure the server is running.");
                }
            }
        });

        panel.add(leftPanel);
        panel.add(rightPanel);

        return panel;
    }

    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JList<String> notificationsList = new JList<>(notificationsModel);

        JButton deleteSelectedBtn = createBlueButton("Delete Selected Notification");
        JButton clearAllBtn = createBlueButton("Delete All Read Notifications");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(deleteSelectedBtn);
        buttonPanel.add(clearAllBtn);

        deleteSelectedBtn.addActionListener(e -> {
            String selected = notificationsList.getSelectedValue();

            if (selected != null) {
                notificationsModel.removeElement(selected);
            }
        });

        clearAllBtn.addActionListener(e -> notificationsModel.clear());

        panel.add(createLabel("Notifications"), BorderLayout.NORTH);
        panel.add(new JScrollPane(notificationsList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CartShareGUI::new);
    }
}