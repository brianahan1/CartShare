/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CartShareGUI extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JButton homeBtn, ledgerBtn, groupsBtn, accountBtn, notificationsBtn, logoutBtn;
    private JLabel usernameLabel;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;

    private String currentUser = "";
    private double currentBalance = 0.0;

    private String selectedCurrency = "USD";
    private String currencySymbol = "$";
    private double currencyRate = 1.0;

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<GroupData> groups = new ArrayList<>();
    private ArrayList<Double> activeChargeAmounts = new ArrayList<>();

    private DefaultListModel<String> groupListModel = new DefaultListModel<>();
    private DefaultListModel<String> memberListModel = new DefaultListModel<>();
    private DefaultListModel<String> itemListModel = new DefaultListModel<>();
    private DefaultListModel<String> activeChargesModel = new DefaultListModel<>();
    private DefaultListModel<String> paidChargesModel = new DefaultListModel<>();
    private DefaultListModel<String> notificationsModel = new DefaultListModel<>();

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
        ledgerBtn.addActionListener(e -> cardLayout.show(mainPanel, "LEDGER"));
        groupsBtn.addActionListener(e -> cardLayout.show(mainPanel, "GROUPS"));
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
        updateHomeLabels();

        setButtonsActive(false);

        if (cardLayout != null && mainPanel != null) {
            cardLayout.show(mainPanel, "ACCOUNT");
        }
    }

    private void updateHomeLabels() {
        if (welcomeLabel != null && balanceLabel != null) {
            welcomeLabel.setText("Welcome, " + currentUser);
            balanceLabel.setText("Current Balance: " + formatMoney(currentBalance));
        }
    }

    private void setCurrency(String currency) {
        selectedCurrency = currency;

        if (currency.equals("USD")) {
            currencySymbol = "$";
            currencyRate = 1.0;
        } else if (currency.equals("GBP")) {
            currencySymbol = "£";
            currencyRate = 0.80;
        } else if (currency.equals("EURO")) {
            currencySymbol = "€";
            currencyRate = 0.93;
        }

        updateHomeLabels();
    }

    private String formatMoney(double usdAmount) {
        double convertedAmount = usdAmount * currencyRate;
        return currencySymbol + String.format("%.2f", convertedAmount);
    }

    private void addNotification(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        String timestamp = LocalDateTime.now().format(formatter);
        notificationsModel.addElement("[" + timestamp + "] " + message);
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

                users.add(new User(username, password, balance));
                addNotification("Account created for " + username);

                signUpUsername.setText("");
                signUpPassword.setText("");
                startingBalance.setText("");

                JOptionPane.showMessageDialog(this, "User signed up successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid sign up information.");
            }
        });

        loginButton.addActionListener(e -> {
            String username = loginUsername.getText();
            String password = new String(loginPassword.getPassword());

            for (User user : users) {
                if (user.username.equals(username) && user.password.equals(password)) {
                    currentUser = user.username;
                    currentBalance = user.balance;

                    setCurrency((String) currencyBox.getSelectedItem());

                    usernameLabel.setText(currentUser);
                    updateHomeLabels();
                    setButtonsActive(true);

                    loginUsername.setText("");
                    loginPassword.setText("");

                    addNotification(currentUser + " logged in using " + selectedCurrency);
                    cardLayout.show(mainPanel, "HOME");
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Incorrect username or password.");
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

        JList<String> groupList = new JList<>(groupListModel);
        JList<String> memberList = new JList<>(memberListModel);
        JList<String> itemList = new JList<>(itemListModel);

        JTextField groupNameField = new JTextField();
        JTextField memberField = new JTextField();
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
        memberRow.add(memberField);
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
            String member = memberField.getText();

            if (index >= 0 && !member.isEmpty()) {
                groups.get(index).members.add(member);
                memberListModel.addElement(member);
                addNotification(member + " added to " + groups.get(index).name);
                memberField.setText("");
            }
        });

        addItemBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();
            String item = itemNameField.getText();
            String price = itemPriceField.getText();

            if (index >= 0 && !item.isEmpty() && !price.isEmpty()) {
                try {
                    double priceAmount = Double.parseDouble(price);
                    String itemLine = item + " - " + formatMoney(priceAmount);

                    groups.get(index).items.add(new ItemData(item, priceAmount));
                    itemListModel.addElement(itemLine);

                    addNotification("Item added to " + groups.get(index).name + ": " + itemLine);

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
        itemListModel.addElement(scannedReceipt.name + " - " + formatMoney(scannedReceipt.price));

        addNotification("Photo uploaded: " + fileName);
        addNotification("Scanned receipt added to " + groups.get(index).name + ": " + formatMoney(53.00));

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

            activeChargesModel.addElement(group.name + " charge: " + formatMoney(split));
            activeChargeAmounts.add(split);

            addNotification("New charge from " + group.name + ": " + formatMoney(split));

            JOptionPane.showMessageDialog(this,
                    "Total: " + formatMoney(total)
                            + "\nMembers: " + memberCount
                            + "\nEach person owes: " + formatMoney(split));
        });

        deleteGroupBtn.addActionListener(e -> {
            int index = groupList.getSelectedIndex();

            if (index >= 0) {
                addNotification("Group deleted: " + groups.get(index).name);

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
                String removedItem = groups.get(groupIndex).items.get(itemIndex).name;

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
                memberListModel.size();
                itemListModel.addElement(item.name + " - " + formatMoney(item.price));
            }
        }
    }

    private JPanel createLedgerPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JList<String> activeCharges = new JList<>(activeChargesModel);
        JList<String> paidCharges = new JList<>(paidChargesModel);

        JButton payButton = createBlueButton("Pay Selected Charge");

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.add(createLabel("Active Charges"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(activeCharges), BorderLayout.CENTER);
        leftPanel.add(payButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        rightPanel.add(createLabel("Paid Charges"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(paidCharges), BorderLayout.CENTER);

        payButton.addActionListener(e -> {
            int index = activeCharges.getSelectedIndex();

            if (index >= 0) {
                String charge = activeChargesModel.get(index);
                double amount = activeChargeAmounts.get(index);

                currentBalance -= amount;
                updateHomeLabels();

                activeChargesModel.remove(index);
                activeChargeAmounts.remove(index);
                paidChargesModel.addElement(charge);

                addNotification("Charge paid: " + charge);
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

    static class User {
        String username;
        String password;
        double balance;

        User(String username, String password, double balance) {
            this.username = username;
            this.password = password;
            this.balance = balance;
        }
    }

    static class GroupData {
        String name;
        ArrayList<String> members = new ArrayList<>();
        ArrayList<ItemData> items = new ArrayList<>();

        GroupData(String name) {
            this.name = name;
        }
    }

    static class ItemData {
        String name;
        double price;

        ItemData(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CartShareGUI::new);
    }
}