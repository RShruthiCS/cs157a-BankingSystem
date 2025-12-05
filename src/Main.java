import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
            return;
        }

        String url  = "jdbc:postgresql://localhost:1227/bankingsystem";
        String user = "postgres";
        String dbpassword = "Raji@1973";


        try (Connection conn = DriverManager.getConnection(url, user, dbpassword)) {
            if (conn != null) {
                System.out.println("Connection to PostgreSQL has been established.");
                System.out.println("Connection to: " + conn.getMetaData().getURL());
                Statement init = conn.createStatement();

                //Initialize DB schema from resource file
                initializeDatabase(conn);
                //Populate sample data from resource file - the TRUNCATE command at the start of initialize_data.sql ensures fresh data each run
                populateSampleData(conn);
                //Default Banker for devs to use
                init.executeUpdate("INSERT INTO Bankers (FirstName, LastName, Title, Username, Password, Email, PhoneNumber) " +
                                    "VALUES ('Site', 'Admin', 'Default Manager', 'admin', 'adminpass', 'adminemail','123-456-7890')");

                SQLFunctions manager = new SQLFunctions(conn);
                Scanner scanner = new Scanner(System.in);

                boolean isBanker = false;
                boolean isCustomer = false;
                Customer sessionCust = null;
                boolean loginValid = false;
                System.out.println("Welcome to the Banking System");
                System.out.println("Logging in as: ");
                System.out.println("1. Banker");
                System.out.println("2. Customer");

                int userType = scanner.nextInt();
                scanner.nextLine(); // consume newline

                while (!loginValid) {
                    if (userType != 1 && userType != 2) {
                        System.out.println("Invalid selection. Proceeding as Customer by default.");
                        userType = 2;
                    }
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    switch (userType) {
                        case 1:
                            loginValid = manager.validateBankerLogin(username, password);
                            if (loginValid) 
                                isBanker = true;
                            break;
                        case 2:
                            sessionCust = manager.validateCustomerLogin(username, password);
                            if (sessionCust != null) {
                                loginValid = true;
                                isCustomer = true;
                            }
                            break;
                    }
                }

                //CORE APPLICATION HERE - Bankers (full access to database)
                while (isBanker) {
                    System.out.println("\nSelect an option:");
                    System.out.println("1. View Data");  
                    System.out.println("2. Insert Data");
                    System.out.println("3. Update Data");
                    System.out.println("4. Delete Data");
                    System.out.println("5. Exit");
                    System.out.print("Enter choice: ");
                    
                    if (scanner.hasNextInt()) {
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // consume newline so scanner.nextLine() works properly
                        switch (choice) {
                            case 1: //VIEW - SELECT QUERIES
                                //get table
                                System.out.println("Enter table name to view data (Customers, Accounts, Transactions, Loans, Bankers, Beneficiaries):");  
                                String tableName = scanner.nextLine().trim();
                                //get attributes
                                String[] attrs = null;
                                System.out.println("Select a sub-list of attributes to include. Enter nothing to select all columns.");
                                System.out.print("Available attributes (comma separated): ");
                                switch (tableName) {
                                    case "Customers":
                                        System.out.println("CustomerID, Username, Password, FirstName, LastName, Email, PhoneNumber, MailingAddress, DateOfBirth, SSN");
                                        break;
                                    case "Accounts":
                                        System.out.println("AccountID, CustomerID, AccountBalance, AccountType, DateOpened");
                                        break;
                                    case "Transactions":
                                        System.out.println("TransactionID, AccountID, TransactionType, Amount, TransactionDate");
                                        break;
                                    case "Loans":
                                        System.out.println("LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate");
                                        break;
                                    case "Bankers":
                                        System.out.println("BankerID, FirstName, LastName, Title, Username, Password, Email, PhoneNumber");
                                        break;
                                    case "Beneficiaries":
                                        System.out.println("BeneficiaryID, CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber");
                                        break;
                                    default:
                                        System.out.println("Invalid table name.");
                                        continue;
                                }
                                String input = scanner.nextLine().trim();
                                if (input.isEmpty()) {
                                    attrs = new String[]{"*"};
                                } else {
                                    attrs = input.split(", ");
                                }
                                for (int i = 0; i < attrs.length; i++) {
                                    attrs[i] = attrs[i].trim();
                                }
                                    
                                //get condition
                                System.out.println("Condition to filter by: (Must be written in SQL syntax, e.g., FirstName = 'John')(Enter nothing to view all rows):");
                                String condition = scanner.nextLine().trim();
                                if (condition.isEmpty()) {
                                    condition = null;
                                }

                                //call select
                                manager.Select(attrs, tableName, condition);
                                break;
                            case 2: //INSERT 
                                //get table to insert into
                                System.out.println("Enter table name to insert data into (Customers, Accounts, Transactions, Loans, Bankers, Beneficiaries):");  
                                String insertTable = scanner.nextLine().trim();
                                //call insert
                                switch (insertTable) {
                                    case "Customers":
                                        manager.insertCustomer();
                                        break;
                                    case "Accounts":
                                        manager.insertAccount();
                                        break;
                                    case "Transactions":
                                        manager.insertTransaction();
                                        break;
                                    case "Loans":
                                        manager.insertLoan();
                                        break;
                                    case "Bankers":
                                        manager.insertBanker();
                                        break;
                                    case "Beneficiaries":
                                        manager.insertBeneficiary();
                                        break;
                                    default:
                                        System.out.println("Invalid table name.");
                                }
                                break;
                            case 3: //UPDATE
                                //System.out.println("Update functionality not implemented yet.");
                                //get table
                                System.out.println("Enter table name to update data in (Customers, Accounts, Transactions, Loans, Bankers, Beneficiaries):");
                                String updateTable = scanner.nextLine().trim();
                                //get attribute
                                System.out.print("Enter single attribute to update: ");
                                switch (updateTable) {
                                    case "Customers":
                                        System.out.println("CustomerID, Username, Password, FirstName, LastName, Email, PhoneNumber, MailingAddress, DateOfBirth, SSN");
                                        break;
                                    case "Accounts":
                                        System.out.println("AccountID, CustomerID, AccountBalance, AccountType, DateOpened");
                                        break;
                                    case "Transactions":
                                        System.out.println("TransactionID, AccountID, TransactionType, Amount, TransactionDate");
                                        break;
                                    case "Loans":
                                        System.out.println("LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate");
                                        break;
                                    case "Bankers":
                                        System.out.println("BankerID, FirstName, LastName, Title, Username, Password, Email, PhoneNumber");
                                        break;
                                    case "Beneficiaries":
                                        System.out.println("BeneficiaryID, CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber");
                                        break;
                                    default:
                                        System.out.println("Invalid table name.");
                                        continue;
                                }
                                String attribute = scanner.nextLine().trim();
                                //get new value
                                System.out.println("Enter new value for " + attribute + ": ");
                                String newValue = scanner.nextLine().trim();
                                //get condition
                                System.out.println("Enter condition to specify which records to update (must be in SQL boolean syntax e.g., CustomerID = 1): ");
                                String idToUpdate = scanner.nextLine().trim();

                                //call update
                                manager.Update(updateTable, attribute, newValue, idToUpdate);

                                break;
                            case 4: //DELETE
                                //System.out.println("Delete functionality not implemented yet.");
                                System.out.println("Enter table name to delete data from (Customers, Accounts, Transactions, Loans, Bankers, Beneficiaries):");  
                                String deleteTable = scanner.nextLine().trim();
                                System.out.println("Enter id number to specify which record to delete (e.g., 1):");
                                int idToDelete = scanner.nextInt();
                                scanner.nextLine(); // consume newline
                                //call delete
                                switch (deleteTable) {
                                    case "Customers":
                                        manager.deleteCustomer(idToDelete);
                                        break;
                                    case "Accounts":
                                        manager.deleteAccount(idToDelete);
                                        break;
                                    case "Transactions":
                                        manager.deleteTransaction(idToDelete);
                                        break;
                                    case "Loans":
                                        manager.deleteLoan(idToDelete);
                                        break;
                                    case "Bankers":
                                        manager.deleteBanker(idToDelete);
                                        break;
                                    case "Beneficiaries":
                                        manager.deleteBeneficiary(idToDelete);
                                        break;
                                    default:
                                        System.out.println("Invalid table name.");
                                }
                                break;
                            case 5: //EXIT
                                isBanker = false;
                                System.out.println("Exiting...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine(); // clear invalid input
                    }
                }
                
                //CORE APPLICATION HERE - Customers (limited access to their own data)
                while (isCustomer) {
                    //System.out.println("Customer functionality not implemented yet. Exiting...");
                    System.out.println("\nSelect an option:");
                    System.out.println("1. Manage Your Accounts");   //view account balances, open/close accounts
                    System.out.println("2. Set Your Beneficiaries"); //add/remove beneficiaries
                    System.out.println("3. Take a Loan");            //apply for a loan
                    System.out.println("4. Make a Transaction");     //deposit, withdraw, transfer after selecting an account
                    System.out.println("5. Exit");
                    System.out.print("Enter choice: ");

                    if (scanner.hasNextInt()) {
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // consume newline so scanner.nextLine() works properly
                        switch (choice) {
                            case 1: //MANAGE ACCOUNTS
                                System.out.println("Select an option:");
                                System.out.println("1. View Account Information");
                                System.out.println("2. Open New Account");
                                System.out.println("3. Close Existing Account");
                                System.out.print("Enter choice: ");
                                int accChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                switch (accChoice) {
                                    case 1: //VIEW BALANCES
                                        manager.Select(new String[]{"AccountID", "AccountBalance", "AccountType", "DateOpened"}, "Accounts", "CustomerID = " + sessionCust.customerID);
                                        break;
                                    case 2: //OPEN ACCOUNT
                                        manager.insertAccount(sessionCust.customerID);
                                        break;
                                    case 3: //CLOSE ACCOUNT
                                        System.out.print("Enter AccountID of the account to close: ");
                                        int accIDToClose = scanner.nextInt();   
                                        scanner.nextLine(); // consume newline
                                        manager.deleteAccount(accIDToClose, sessionCust.customerID);
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Returning to main menu.");
                                }
                                break;
                            case 2: //SET BENEFICIARIES
                                System.out.println("Select an option:");
                                System.out.println("1. View Existing Beneficiaries");
                                System.out.println("2. Set a Beneficiary");
                                System.out.println("3. Drop a Beneficiary");
                                System.out.print("Enter choice: ");
                                int beneficChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                switch (beneficChoice) {
                                    case 1: //VIEW BENEFICIARIES
                                        manager.Select(new String[]{"BeneficiaryID", "FirstName", "LastName", "Relationship", "MailingAddress", "PhoneNumber"}, "Beneficiaries", "CustomerID = " + sessionCust.customerID);
                                        break;
                                    case 2: //SET BENEFICIARY
                                        manager.insertBeneficiary(sessionCust.customerID);
                                        break;
                                    case 3: //DROP BENEFICIARY
                                        System.out.print("Enter BeneficiaryID of the beneficiary to drop: ");
                                        int benefIDToDrop = scanner.nextInt();
                                        scanner.nextLine(); // consume newline
                                        manager.deleteBeneficiary(benefIDToDrop, sessionCust.customerID);
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Returning to main menu.");
                                }
                                break;
                            case 3: //TAKE A LOAN
                                System.out.println("Select an option:");
                                System.out.println("1. View Existing Loans");
                                System.out.println("2. Apply for a New Loan");
                                System.out.print("Enter choice: ");
                                int loanChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                switch(loanChoice) {
                                    case 1: //VIEW LOANS
                                        manager.Select(new String[]{"LoanID", "LoanAmount", "InterestRate", "StartDate", "EndDate"}, "Loans", "CustomerID = " + sessionCust.customerID);
                                        break;
                                    case 2: //APPLY FOR LOAN
                                        manager.insertLoan(sessionCust.customerID);
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Returning to main menu.");
                                }
                                break;
                            case 4: //MAKE A TRANSACTION
                                System.out.println("Select an option:");
                                System.out.println("1. Withdraw from an Account");
                                System.out.println("2. Deposit into an Account");
                                System.out.println("3. Transfer between Accounts");
                                System.out.print("Enter choice: ");
                                int transChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                switch (transChoice) {
                                    case 1: //WITHDRAW
                                        System.out.println("Displaying your accounts:");
                                        manager.Select(new String[]{"AccountID", "AccountBalance", "AccountType"}, "Accounts", "CustomerID = " + sessionCust.customerID);
                                        System.out.print("Enter AccountID to withdraw from: ");
                                        int withAccID = scanner.nextInt();
                                        scanner.nextLine();
                                        System.out.print("Enter amount to withdraw: ");
                                        double withAmount = scanner.nextDouble();
                                        scanner.nextLine();
                                        manager.withdrawBalance(withAccID, sessionCust.customerID, withAmount);
                                        break;

                                    case 2: //DEPOSIT
                                        System.out.println("Displaying your accounts:");
                                        manager.Select(new String[]{"AccountID", "AccountBalance", "AccountType"}, "Accounts", "CustomerID = " + sessionCust.customerID);
                                        System.out.print("Enter AccountID to deposit into: ");
                                        int depoAccID = scanner.nextInt();
                                        scanner.nextLine(); // consume newline
                                        System.out.print("Enter amount to deposit: ");
                                        double depoAmount = scanner.nextDouble();
                                        scanner.nextLine(); // consume newline
                                        manager.depositBalance(depoAccID, sessionCust.customerID, depoAmount);
                                        break;
                                    case 3: //TRANSFER
                                        System.out.println("Displaying your accounts:");
                                        manager.Select(new String[]{"AccountID", "AccountBalance", "AccountType"}, "Accounts", "CustomerID = " + sessionCust.customerID);
                                        System.out.print("Enter AccountID to transfer from: ");
                                        int transferFromID = scanner.nextInt();
                                        scanner.nextLine(); // consume newline
                                        System.out.print("Enter AccountID to transfer into: ");
                                        int transferIntoID = scanner.nextInt();
                                        scanner.nextLine(); // consume newline
                                        System.out.print("Enter amount to transfer: ");
                                        double transferAmount = scanner.nextDouble();
                                        scanner.nextLine(); // consume newline
                                        manager.transferBalance(transferFromID, transferIntoID, sessionCust.customerID, transferAmount);
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Returning to main menu.");
                                }
                                break;
                            case 5: //EXIT
                                isCustomer = false;
                                System.out.println("Exiting...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine(); // clear invalid input
                    }
                }

                scanner.close();
                manager.scanner.close();
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeDatabase(Connection conn) throws Exception {
        // Get the input stream for the resource file
        InputStream is = Main.class.getResourceAsStream("/create_schema.sql");
        if (is == null) {
            throw new IllegalArgumentException("SQL file not found in resources folder");
        }

        String sqlScript;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            // Read the entire file content into a single string
            sqlScript = reader.lines().collect(Collectors.joining("\n"));
        }

        // Execute the entire script
        Statement init = conn.createStatement();
        init.execute(sqlScript); // Use execute() as it handles multiple DDL statements
        init.close();
        System.out.println("Database schema initialized successfully.");
    }

    public static void populateSampleData(Connection conn) throws Exception {
        // Similar to initializeDatabase, read another SQL file for sample data
        InputStream is = Main.class.getResourceAsStream("/initialize_data.sql");
        if (is == null) {
            throw new IllegalArgumentException("Sample data SQL file not found in resources folder");
        }

        String sqlScript;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            sqlScript = reader.lines().collect(Collectors.joining("\n"));
        }

        String[] statements = sqlScript.split(";\\s*\\n");
        for (String statement : statements) {
            System.out.println("Executing: " + statement);
        }

        try (Statement init = conn.createStatement()) {
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    init.execute(statement);
                }
            }
            init.close();
        } catch (SQLException e) {
            System.out.println("Error populating sample data: " + e.getMessage());
            throw e;
        }

        System.out.println("Sample data populated successfully.");
    }
}