import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BankingSystemGUI {
    private Connection conn;
    private SQLFunctions sqlFunctions;

    public BankingSystemGUI() {
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:1227/bankingsystem";
            String user = "postgres";
            String password = "Raji@1973";
            conn = DriverManager.getConnection(url, user, password);
            sqlFunctions = new SQLFunctions(conn);
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    public void showLoginWindow() {
        JFrame loginFrame = new JFrame("Banking System - Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome to Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Login as:"), gbc);

        JComboBox<String> userTypeBox = new JComboBox<>(new String[]{"Customer", "Banker"});
        gbc.gridx = 1;
        panel.add(userTypeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeBox.getSelectedItem();

            if (userType.equals("Banker")) {
                if (sqlFunctions.validateBankerLogin(username, password)) {
                    loginFrame.dispose();
                    showBankerDashboard();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!");
                }
            } else {
                Customer customer = sqlFunctions.validateCustomerLogin(username, password);
                if (customer != null) {
                    loginFrame.dispose();
                    showCustomerDashboard(customer);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!");
                }
            }
        });

        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }

    private void showBankerDashboard() {
        JFrame frame = new JFrame("Banker Dashboard");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Customers", createBankerTablePanel("Customers"));
        tabbedPane.addTab("Accounts", createBankerTablePanel("Accounts"));
        tabbedPane.addTab("Transactions", createBankerTablePanel("Transactions"));
        tabbedPane.addTab("Loans", createBankerTablePanel("Loans"));
        tabbedPane.addTab("Bankers", createBankerTablePanel("Bankers"));
        tabbedPane.addTab("Beneficiaries", createBankerTablePanel("Beneficiaries"));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createBankerTablePanel(String tableName) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewButton = new JButton("View All");
        JButton insertButton = new JButton("Insert");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        viewButton.addActionListener(e -> loadTableData(tableName, model));
        refreshButton.addActionListener(e -> loadTableData(tableName, model));

        insertButton.addActionListener(e -> {
            switch (tableName) {
                case "Customers":
                    showInsertCustomerDialog();
                    break;
                case "Accounts":
                    showInsertAccountDialog();
                    break;
                case "Transactions":
                    showInsertTransactionDialog();
                    break;
                case "Loans":
                    showInsertLoanDialog();
                    break;
                case "Bankers":
                    showInsertBankerDialog();
                    break;
                case "Beneficiaries":
                    showInsertBeneficiaryDialog();
                    break;
            }
            loadTableData(tableName, model);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(panel, 
                    "Are you sure you want to delete this record?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    switch (tableName) {
                        case "Customers":
                            sqlFunctions.deleteCustomer(id);
                            break;
                        case "Accounts":
                            sqlFunctions.deleteAccount(id);
                            break;
                        case "Transactions":
                            sqlFunctions.deleteTransaction(id);
                            break;
                        case "Loans":
                            sqlFunctions.deleteLoan(id);
                            break;
                        case "Bankers":
                            sqlFunctions.deleteBanker(id);
                            break;
                        case "Beneficiaries":
                            sqlFunctions.deleteBeneficiary(id);
                            break;
                    }
                    loadTableData(tableName, model);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a row to delete");
            }
        });

        updateButton.addActionListener(e -> {
            String attribute = JOptionPane.showInputDialog(panel, "Enter attribute to update:");
            String newValue = JOptionPane.showInputDialog(panel, "Enter new value:");
            String condition = JOptionPane.showInputDialog(panel, "Enter condition (e.g., CustomerID = 1):");
            
            if (attribute != null && newValue != null && condition != null) {
                sqlFunctions.Update(tableName, attribute, newValue, condition);
                loadTableData(tableName, model);
            }
        });

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadTableData(String tableName, DefaultTableModel model) {
        try {
            model.setRowCount(0);
            model.setColumnCount(0);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    private void showInsertCustomerDialog() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField ssnField = new JTextField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("DOB (YYYY-MM-DD):"));
        panel.add(dobField);
        panel.add(new JLabel("SSN:"));
        panel.add(ssnField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Customer", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO Customers (Username, Password, FirstName, LastName, Email, PhoneNumber, MailingAddress, DateOfBirth, SSN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, usernameField.getText());
                pstmt.setString(2, passwordField.getText());
                pstmt.setString(3, firstNameField.getText());
                pstmt.setString(4, lastNameField.getText());
                pstmt.setString(5, emailField.getText());
                pstmt.setString(6, phoneField.getText());
                pstmt.setString(7, addressField.getText());
                pstmt.setDate(8, Date.valueOf(dobField.getText()));
                pstmt.setString(9, ssnField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Customer added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void showInsertAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField customerIdField = new JTextField();
        JComboBox<String> accountTypeBox = new JComboBox<>(new String[]{"Checking", "Savings"});
        JTextField balanceField = new JTextField("0.00");

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("Account Type:"));
        panel.add(accountTypeBox);
        panel.add(new JLabel("Balance:"));
        panel.add(balanceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Account", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO Accounts (CustomerID, AccountType, AccountBalance) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(customerIdField.getText()));
                pstmt.setString(2, (String) accountTypeBox.getSelectedItem());
                pstmt.setDouble(3, Double.parseDouble(balanceField.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Account added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void showInsertTransactionDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField customerIdField = new JTextField();
        JTextField fromAccountIdField = new JTextField();
        JTextField toAccountIdField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Deposit", "Withdrawal", "Transfer"});
        JTextField amountField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);

        panel.add(new JLabel("From Account ID:"));
        panel.add(fromAccountIdField);

        panel.add(new JLabel("To Account ID (for Transfer):"));
        panel.add(toAccountIdField);

        panel.add(new JLabel("Type:"));
        panel.add(typeBox);

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Insert Transaction",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int customerId = Integer.parseInt(customerIdField.getText());
                int fromAccountId = Integer.parseInt(fromAccountIdField.getText());
                double amount = Double.parseDouble(amountField.getText());
                String type = (String) typeBox.getSelectedItem();

                if (type.equalsIgnoreCase("Deposit")) {
                    sqlFunctions.depositBalance(fromAccountId, customerId, amount);
                    JOptionPane.showMessageDialog(null, "Deposit completed successfully.");
                } else if (type.equalsIgnoreCase("Withdrawal")) {
                    sqlFunctions.withdrawBalance(fromAccountId, customerId, amount);
                    JOptionPane.showMessageDialog(null, "Withdrawal completed successfully.");
                } else if (type.equalsIgnoreCase("Transfer")) {
                    int toAccountId = Integer.parseInt(toAccountIdField.getText());
                    sqlFunctions.transferBalance(fromAccountId, toAccountId, customerId, amount);
                    JOptionPane.showMessageDialog(null, "Transfer completed successfully.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        };

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int customerId = Integer.parseInt(customerIdField.getText());
                    int fromAccountId = Integer.parseInt(fromAccountIdField.getText());
                    double amount = Double.parseDouble(amountField.getText());
                    String type = (String) typeBox.getSelectedItem();

                    if (type.equalsIgnoreCase("Deposit")) {
                        sqlFunctions.depositBalance(fromAccountId, customerId, amount);
                        JOptionPane.showMessageDialog(null, "Deposit completed successfully.");
                    } else if (type.equalsIgnoreCase("Withdrawal")) {
                        sqlFunctions.withdrawBalance(fromAccountId, customerId, amount);
                        JOptionPane.showMessageDialog(null, "Withdrawal completed successfully.");
                    } else if (type.equalsIgnoreCase("Transfer")) {
                        int toAccountId = Integer.parseInt(toAccountIdField.getText());
                        sqlFunctions.transferBalance(fromAccountId, toAccountId, customerId, amount);
                        JOptionPane.showMessageDialog(null, "Transfer completed successfully.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            }
        }


    private void showInsertLoanDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField customerIdField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField rateField = new JTextField();
        JTextField endDateField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("Loan Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Interest Rate:"));
        panel.add(rateField);
        panel.add(new JLabel("End Date (YYYY-MM-DD):"));
        panel.add(endDateField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Loan", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO Loans (CustomerID, LoanAmount, InterestRate, EndDate) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(customerIdField.getText()));
                pstmt.setDouble(2, Double.parseDouble(amountField.getText()));
                pstmt.setDouble(3, Double.parseDouble(rateField.getText()));
                pstmt.setDate(4, Date.valueOf(endDateField.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Loan added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void showInsertBankerDialog() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Banker", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO Bankers (FirstName, LastName, Title, Username, Password, Email, PhoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, firstNameField.getText());
                pstmt.setString(2, lastNameField.getText());
                pstmt.setString(3, titleField.getText());
                pstmt.setString(4, usernameField.getText());
                pstmt.setString(5, passwordField.getText());
                pstmt.setString(6, emailField.getText());
                pstmt.setString(7, phoneField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Banker added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void showInsertBeneficiaryDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField customerIdField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField relationshipField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Relationship:"));
        panel.add(relationshipField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Beneficiary", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO Beneficiaries (CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(customerIdField.getText()));
                pstmt.setString(2, firstNameField.getText());
                pstmt.setString(3, lastNameField.getText());
                pstmt.setString(4, relationshipField.getText());
                pstmt.setString(5, addressField.getText());
                pstmt.setString(6, phoneField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Beneficiary added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void showCustomerDashboard(Customer customer) {
        JFrame frame = new JFrame("Customer Dashboard - " + customer.firstName + " " + customer.lastName);
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("My Accounts", createCustomerAccountPanel(customer));
        tabbedPane.addTab("Beneficiaries", createCustomerBeneficiaryPanel(customer));
        tabbedPane.addTab("Loans", createCustomerLoanPanel(customer));
        tabbedPane.addTab("Transactions", createCustomerTransactionPanel(customer));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createCustomerAccountPanel(Customer customer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewButton = new JButton("View My Accounts");
        JButton openButton = new JButton("Open New Account");
        JButton closeButton = new JButton("Close Account");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(openButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(refreshButton);

        viewButton.addActionListener(e -> {
            try {
                model.setRowCount(0);
                model.setColumnCount(0);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT AccountID, AccountBalance, AccountType, DateOpened FROM Accounts WHERE CustomerID = " + customer.customerID);
                ResultSetMetaData metaData = rs.getMetaData();
                
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }

                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        openButton.addActionListener(e -> {
            String[] options = {"Checking", "Savings"};
            String accountType = (String) JOptionPane.showInputDialog(panel, 
                "Select Account Type:", "Open New Account", JOptionPane.QUESTION_MESSAGE, 
                null, options, options[0]);
            
            if (accountType != null) {
                try {
                    String sql = "INSERT INTO Accounts (CustomerID, AccountType) VALUES (?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, customer.customerID);
                    pstmt.setString(2, accountType);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Account opened successfully!");
                    viewButton.doClick();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        closeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int accountId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(panel, 
                    "Are you sure you want to close this account?", 
                    "Confirm", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    sqlFunctions.deleteAccount(accountId, customer.customerID);
                    viewButton.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select an account");
            }
        });

        refreshButton.addActionListener(e -> viewButton.doClick());


        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCustomerBeneficiaryPanel(Customer customer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewButton = new JButton("View Beneficiaries");
        JButton addButton = new JButton("Add Beneficiary");
        JButton removeButton = new JButton("Remove Beneficiary");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        

        viewButton.addActionListener(e -> {
            try {
                model.setRowCount(0);
                model.setColumnCount(0);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT BeneficiaryID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber FROM Beneficiaries WHERE CustomerID = " + customer.customerID);
                ResultSetMetaData metaData = rs.getMetaData();
                
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }

                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        addButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField relationshipField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField phoneField = new JTextField();

            inputPanel.add(new JLabel("First Name:"));
            inputPanel.add(firstNameField);
            inputPanel.add(new JLabel("Last Name:"));
            inputPanel.add(lastNameField);
            inputPanel.add(new JLabel("Relationship:"));
            inputPanel.add(relationshipField);
            inputPanel.add(new JLabel("Address:"));
            inputPanel.add(addressField);
            inputPanel.add(new JLabel("Phone:"));
            inputPanel.add(phoneField);

            int result = JOptionPane.showConfirmDialog(panel, inputPanel, "Add Beneficiary", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String sql = "INSERT INTO Beneficiaries (CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, customer.customerID);
                    pstmt.setString(2, firstNameField.getText());
                    pstmt.setString(3, lastNameField.getText());
                    pstmt.setString(4, relationshipField.getText());
                    pstmt.setString(5, addressField.getText());
                    pstmt.setString(6, phoneField.getText());
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Beneficiary added successfully!");
                    viewButton.doClick();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int beneficiaryId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(panel, 
                    "Are you sure you want to remove this beneficiary?", 
                    "Confirm", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    sqlFunctions.deleteBeneficiary(beneficiaryId, customer.customerID);
                    viewButton.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a beneficiary");
            }
        });

        refreshButton.addActionListener(e -> viewButton.doClick());

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCustomerLoanPanel(Customer customer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewButton = new JButton("View My Loans");
        JButton applyButton = new JButton("Apply for Loan");
        JButton payButton = new JButton("Pay Loan");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(payButton);
        buttonPanel.add(refreshButton);
        viewButton.addActionListener(e -> {
            try {
                model.setRowCount(0);
                model.setColumnCount(0);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT LoanID, LoanAmount, InterestRate, Status, StartDate, EndDate " +
                    "FROM Loans WHERE CustomerID = " + customer.customerID);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });


        applyButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField amountField = new JTextField();
            JTextField rateField = new JTextField();

            inputPanel.add(new JLabel("Loan Amount:"));
            inputPanel.add(amountField);
            inputPanel.add(new JLabel("Proposed Interest Rate:"));
            inputPanel.add(rateField);

            int result = JOptionPane.showConfirmDialog(panel, inputPanel, "Apply for Loan", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String sql = "INSERT INTO Loans (CustomerID, LoanAmount, InterestRate) VALUES (?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, customer.customerID);
                    pstmt.setDouble(2, Double.parseDouble(amountField.getText()));
                    pstmt.setDouble(3, Double.parseDouble(rateField.getText()));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Loan application submitted successfully!");
                    viewButton.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        payButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(panel, "Please select a loan to pay.");
                return;
            }

            int loanId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

            String accountIdStr = JOptionPane.showInputDialog(panel, "Enter Account ID to pay from:");
            if (accountIdStr == null) {
                return;
            }

            String amountStr = JOptionPane.showInputDialog(panel, "Enter payment amount:");
            if (amountStr == null) {
                return;
            }

            try {
                int accountId = Integer.parseInt(accountIdStr);
                double amount = Double.parseDouble(amountStr);
                sqlFunctions.payLoan(loanId, accountId, customer.customerID, amount);
                viewButton.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });
        refreshButton.addActionListener(e -> viewButton.doClick());


        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCustomerTransactionPanel(Customer customer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton viewTransactionsButton = new JButton("View Transactions");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(viewTransactionsButton);
        buttonPanel.add(refreshButton);

        JTextArea resultArea = new JTextArea(20, 60);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        depositButton.addActionListener(e -> {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT AccountID, AccountBalance, AccountType FROM Accounts WHERE CustomerID = " + customer.customerID);
                
                StringBuilder accounts = new StringBuilder("Your Accounts:\n");
                while (rs.next()) {
                    accounts.append("ID: ").append(rs.getInt("AccountID"))
                            .append(" | Type: ").append(rs.getString("AccountType"))
                            .append(" | Balance: $").append(rs.getDouble("AccountBalance"))
                            .append("\n");
                }
                
                String accountIdStr = JOptionPane.showInputDialog(panel, accounts.toString() + "\nEnter Account ID:");
                if (accountIdStr != null) {
                    String amountStr = JOptionPane.showInputDialog(panel, "Enter amount to deposit:");
                    if (amountStr != null) {
                        int accountId = Integer.parseInt(accountIdStr);
                        double amount = Double.parseDouble(amountStr);
                        sqlFunctions.depositBalance(accountId, customer.customerID, amount);
                        resultArea.append("Deposited $" + amount + " to Account " + accountId + "\n");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT AccountID, AccountBalance, AccountType FROM Accounts WHERE CustomerID = " + customer.customerID);
                StringBuilder accounts = new StringBuilder("Your Accounts:\n");
                while (rs.next()) {
                    accounts.append("ID: ").append(rs.getInt("AccountID"))
                            .append(" | Type: ").append(rs.getString("AccountType"))
                            .append(" | Balance: $").append(rs.getDouble("AccountBalance"))
                            .append("\n");
                }

                String accountIdStr = JOptionPane.showInputDialog(panel, accounts.toString() + "\nEnter Account ID:");
                if (accountIdStr != null) {
                    String amountStr = JOptionPane.showInputDialog(panel, "Enter amount to withdraw:");
                    if (amountStr != null) {
                        int accountId = Integer.parseInt(accountIdStr);
                        double amount = Double.parseDouble(amountStr);
                        sqlFunctions.withdrawBalance(accountId, customer.customerID, amount);
                        resultArea.append("Withdrew $" + amount + " from Account " + accountId + "\n");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        transferButton.addActionListener(e -> {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT AccountID, AccountBalance, AccountType FROM Accounts WHERE CustomerID = " + customer.customerID);
                
                StringBuilder accounts = new StringBuilder("Your Accounts:\n");
                while (rs.next()) {
                    accounts.append("ID: ").append(rs.getInt("AccountID"))
                            .append(" | Type: ").append(rs.getString("AccountType"))
                            .append(" | Balance: $").append(rs.getDouble("AccountBalance"))
                            .append("\n");
                }
                
                String fromIdStr = JOptionPane.showInputDialog(panel, accounts.toString() + "\nEnter Account ID to transfer FROM:");
                if (fromIdStr != null) {
                    String toIdStr = JOptionPane.showInputDialog(panel, "Enter Account ID to transfer TO:");
                    if (toIdStr != null) {
                        String amountStr = JOptionPane.showInputDialog(panel, "Enter amount to transfer:");
                        if (amountStr != null) {
                            int fromId = Integer.parseInt(fromIdStr);
                            int toId = Integer.parseInt(toIdStr);
                            double amount = Double.parseDouble(amountStr);
                            sqlFunctions.transferBalance(fromId, toId, customer.customerID, amount);
                            resultArea.append("Transferred $" + amount + " from Account " + fromId + " to Account " + toId + "\n");
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        viewTransactionsButton.addActionListener(e -> {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT t.TransactionID, t.AccountID, t.TransactionType, t.Amount, t.TransactionDate " +
                        "FROM Transactions t " +
                        "JOIN Accounts a ON t.AccountID = a.AccountID " +
                        "WHERE a.CustomerID = " + customer.customerID + " " +
                        "ORDER BY t.TransactionDate DESC, t.TransactionID DESC"
                );

                StringBuilder sb = new StringBuilder("Your Transactions:\n");
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("TransactionID"))
                    .append(" | Account: ").append(rs.getInt("AccountID"))
                    .append(" | Type: ").append(rs.getString("TransactionType"))
                    .append(" | Amount: $").append(rs.getDouble("Amount"))
                    .append(" | Date: ").append(rs.getTimestamp("TransactionDate"))
                    .append("\n");
                }

                resultArea.append(sb.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        refreshButton.addActionListener(e -> {
            resultArea.setText("");
            viewTransactionsButton.doClick();
        });


        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankingSystemGUI gui = new BankingSystemGUI();
            gui.showLoginWindow();
        });
    }
}