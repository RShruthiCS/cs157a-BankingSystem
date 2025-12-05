import java.sql.*;
import java.util.Scanner;

public class SQLFunctions {
    private Connection conn;
    public Scanner scanner;

    //constructor
    public SQLFunctions(Connection conn) {
        this.conn = conn;
        this.scanner = new Scanner(System.in);
    }

    /* DEVELOPER NOTE (Bryan Morales Sosa): The functions below are organized for different functionalities:
        functionName() - no parameters: used when the user is prompted to input all necessary data (Banker/Admin use)
        functionName(parameters) - with parameters: used when some data is already known (Customer use case)
    */


    //
    // INSERT FUNCTIONS (Bryan Morales Sosa)
    // All of these functions prompt the user for necessary data to insert new records into the database
    // if the function requires an ID parameter, it is assumed that the user is logged in and that ID is known
    // else, the user is assumed to be a banker/admin inserting data for a new entity
    //
    public void insertCustomer() {
        try {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter Mailing Address: ");
            String mailingAddress = scanner.nextLine();
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            String dateOfBirth = scanner.nextLine();
            System.out.print("Enter SSN (XXX-XX-XXXX): ");
            String ssn = scanner.nextLine();

            String sql = "INSERT INTO Customers (Username, Password, FirstName, LastName, Email, PhoneNumber, MailingAddress, DateOfBirth, SSN) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, phoneNumber);
            pstmt.setString(7, mailingAddress);
            pstmt.setDate(8, Date.valueOf(dateOfBirth));
            pstmt.setString(9, ssn);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer inserted successfully.");
            } else {
                System.out.println("Failed to insert customer.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertBanker() {
        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine();

            String sql = "INSERT INTO Bankers (FirstName, LastName, Title, Username, Password, Email, PhoneNumber) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, title);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.setString(6, email);
            pstmt.setString(7, phoneNumber);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Banker inserted successfully.");
            } else {
                System.out.println("Failed to insert banker.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertBeneficiary() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter Relationship: ");
            String relationship = scanner.nextLine();
            System.out.print("Enter Mailing Address: ");
            String mailingAddress = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine();

            String sql = "INSERT INTO Beneficiaries (CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, relationship);
            pstmt.setString(5, mailingAddress);
            pstmt.setString(6, phoneNumber);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Beneficiary inserted successfully.");
            } else {
                System.out.println("Failed to insert beneficiary.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertBeneficiary(int customerID) {
        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter Relationship: ");
            String relationship = scanner.nextLine();
            System.out.print("Enter Mailing Address: ");
            String mailingAddress = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine();

            String sql = "INSERT INTO Beneficiaries (CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerID);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, relationship);
            pstmt.setString(5, mailingAddress);
            pstmt.setString(6, phoneNumber);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Beneficiary inserted successfully.");
            } else {
                System.out.println("Failed to insert beneficiary.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertAccount() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Account Type: ");
            String accountType = scanner.nextLine();
            System.out.print("Enter Balance: ");
            double balance = Double.parseDouble(scanner.nextLine());

            String sql = "INSERT INTO Accounts (CustomerID, AccountType, AccountBalance) " +
                         "VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, accountType);
            pstmt.setDouble(3, balance);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account inserted successfully.");
            } else {
                System.out.println("Failed to insert account.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertAccount(int customerId) {
        try {
            System.out.print("Enter Account Type: (Type either 'Checking' or 'Savings') ");
            String accountType = scanner.nextLine();

            String sql = "INSERT INTO Accounts (CustomerID, AccountType) " +
                         "VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, accountType);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account inserted successfully.");
            } else {
                System.out.println("Failed to insert account.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertTransaction() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Account ID: ");
            int accountId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Transaction Type (Deposit/Withdrawal/Transfer): ");
            String transactionType = scanner.nextLine();

            System.out.print("Enter Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (transactionType.equalsIgnoreCase("Deposit")) {
                depositBalance(accountId, customerId, amount);
            } else if (transactionType.equalsIgnoreCase("Withdrawal")) {
                withdrawBalance(accountId, customerId, amount);
            } else if (transactionType.equalsIgnoreCase("Transfer")) {
                System.out.print("Enter Destination Account ID: ");
                int destinationAccountId = Integer.parseInt(scanner.nextLine());
                transferBalance(accountId, destinationAccountId, customerId, amount);
            } else {
                System.out.println("Unsupported transaction type. Use Deposit, Withdrawal, or Transfer.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input: " + e.getMessage());
        }
    }


    public void insertTransaction(int accountId, String transactionType, double amount) {
        try {
            String sql = "INSERT INTO Transactions (AccountID, TransactionType, Amount) " +
                         "VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction inserted successfully.");
            } else {
                System.out.println("Failed to insert transaction.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void insertLoan() {
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Loan Amount: ");
            double loanAmount = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Interest Rate: ");
            double interestRate = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String endDate = scanner.nextLine();

            String sql = "INSERT INTO Loans (CustomerID, LoanAmount, InterestRate, EndDate) " +
                         "VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, loanAmount);
            pstmt.setDouble(3, interestRate);
            pstmt.setDate(4, Date.valueOf(endDate));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Loan inserted successfully.");
            } else {
                System.out.println("Failed to insert loan.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }   
    public void insertLoan(int customerId) { //customer use 
        try {
            System.out.print("Request Loan Amount: ");
            double loanAmount = Double.parseDouble(scanner.nextLine());
            System.out.print("Propose an Interest Rate: ");
            double interestRate = Double.parseDouble(scanner.nextLine());

            String sql = "INSERT INTO Loans (CustomerID, LoanAmount, InterestRate) " +
                         "VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, loanAmount);
            pstmt.setDouble(3, interestRate);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Loan applied for successfully.");
            } else {
                System.out.println("Failed to apply for loan.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    //
    //
    // UPDATE FUNCTIONS (Bryan Morales Sosa)
    // 
    //
    public void Update(String table, String attribute, String newValue, String condition) {
        try {
            String sql = "UPDATE " + table + " SET " + attribute + " = ? WHERE " + condition;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newValue);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record(s) updated successfully.");
            } else {
                System.out.println("Failed to update record(s).");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    //
    //
    // DELETE FUNCTIONS (Bryan Morales Sosa)
    // these functions delete records from the database based on provided IDs, it is assumed that a banker/admin is performing these actions
    // if a function requires BOTH an entity ID and a customer ID, it is assumed that the customer is performing the action on their own data
    // 
    public void deleteCustomer(int customerId) {
        try {
            String sql = "DELETE FROM Customers WHERE CustomerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Failed to delete customer.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteAccount(int accountId) {
        try {
            String sql = "DELETE FROM Accounts WHERE AccountID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Failed to delete account.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteAccount(int accountId, int customerId) {
        try {
            String sql = "DELETE FROM Accounts WHERE AccountID = ? AND CustomerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, customerId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Failed to delete account. Please ensure the AccountID is correct and belongs to you.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteLoan(int loanId) {
        try {
            String sql = "DELETE FROM Loans WHERE LoanID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, loanId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Loan deleted successfully.");
            } else {
                System.out.println("Failed to delete loan.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteTransaction(int transactionId) {
        try {
            String sql = "DELETE FROM Transactions WHERE TransactionID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, transactionId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction deleted successfully.");
            } else {
                System.out.println("Failed to delete transaction.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteBanker(int bankerId) {
        try {
            String sql = "DELETE FROM Bankers WHERE BankerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bankerId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Banker deleted successfully.");
            } else {
                System.out.println("Failed to delete banker.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteBeneficiary(int beneficiaryId) {
        try {
            String sql = "DELETE FROM Beneficiaries WHERE BeneficiaryID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, beneficiaryId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Beneficiary deleted successfully.");
            } else {
                System.out.println("Failed to delete beneficiary.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void deleteBeneficiary(int beneficiaryId, int customerId) {
        try {
            String sql = "DELETE FROM Beneficiaries WHERE BeneficiaryID = ? AND CustomerID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, beneficiaryId);
            pstmt.setInt(2, customerId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Beneficiary deleted successfully.");
            } else {
                System.out.println("Failed to delete beneficiary. Please ensure the BeneficiaryID is correct and belongs to you.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    //
    //
    //SELECT FUNCTIONS (Bryan Morales Sosa)
    //
    //
    //this function allows for dynamic selection of attributes and conditions, showcasing custom SQL query construction
    public void Select(String[] attrs, String table, String condition) {
        try {       
            String sql = "SELECT " + String.join(", ", attrs) + " FROM " + table;
            if (condition != null) {
                sql += " WHERE " + condition;
            }
            System.out.println("Executing following SQL: " + sql);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int noOfColumns = rsmd.getColumnCount();

            while (rs.next()) {
                System.out.println(" ---------------------------------------------------------------------------- ");
                for (int i = 1; i <= noOfColumns; i++) {
                    System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i) + " | ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    public void viewCustomers() {
        try {
            String sql = "SELECT * FROM Customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("--------------------------------------------------");   
                System.out.println("CustomerID: " + rs.getInt("CustomerID") +
                                   ", Username: " + rs.getString("Username") +
                                   ", FirstName: " + rs.getString("FirstName") +
                                   ", LastName: " + rs.getString("LastName") +
                                   ", Email: " + rs.getString("Email") +
                                   ", PhoneNumber: " + rs.getString("PhoneNumber") +
                                   ", MailingAddress: " + rs.getString("MailingAddress") +
                                   ", DateOfBirth: " + rs.getDate("DateOfBirth") +
                                   ", SSN: " + rs.getString("SSN"));
                System.out.println("--------------------------------------------------");  
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    //
    //
    // BANKING FUNCTIONS (Bryan Morales Sosa)
    // These functions handle banking-specific operations like deposits, withdrawals, and login validations
    //
    public void depositBalance(int accountId, int customerID, double amount) {
        try {
            conn.setAutoCommit(false); //TWO functions: updating balance and inserting transaction, so we use SQL transactions to ensure atomicity
            //String sql = "UPDATE Accounts SET AccountBalance = AccountBalance + ? WHERE CustomerID = ? AND AccountID = ? AND AccountBalance >= ?";
            String sql = "UPDATE Accounts SET AccountBalance = AccountBalance + ? " + "WHERE CustomerID = ? AND AccountID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, customerID);
            pstmt.setInt(3, accountId);
            //pstmt.setDouble(4, amount);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account balance updated successfully.");
                //on successful withdrawal, insert a transaction record
                insertTransaction(accountId, "Deposit", amount);

            } else {
                System.out.println("Failed to update account balance. Aborted.");
                conn.rollback();
                return;
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback Error: " + rollbackEx.getMessage());
            }
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Auto-commit Reset Error: " + ex.getMessage());
            }
        }
    }
    public void withdrawBalance(int accountId, int customerID, double amount) {
        try {
            conn.setAutoCommit(false);

            String sql = "UPDATE Accounts SET AccountBalance = AccountBalance - ? " +
                        "WHERE CustomerID = ? AND AccountID = ? AND AccountBalance >= ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, customerID);
            pstmt.setInt(3, accountId);
            pstmt.setDouble(4, amount);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account balance updated successfully.");
                insertTransaction(accountId, "Withdrawal", amount);
            } else {
                System.out.println("Failed to update account balance. Aborted");
                conn.rollback();
                return;
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback Error: " + rollbackEx.getMessage());
            }
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Auto-commit Reset Error: " + ex.getMessage());
            }
        }
    }

    public void transferBalance(int fromAccountId, int toAccountId, int customerId, double amount) {
        try {
            conn.setAutoCommit(false); //this function involves THREE statements, so we have to use SQL transactions to ensure atomicity

            //withdraw from source account
            String sqlWithdraw = "UPDATE Accounts SET AccountBalance = AccountBalance - ? WHERE CustomerID = ? AND AccountID = ? AND AccountBalance >= ?";
            PreparedStatement pstmtWithdraw = conn.prepareStatement(sqlWithdraw);
            pstmtWithdraw.setDouble(1, amount);
            pstmtWithdraw.setInt(2, customerId);
            pstmtWithdraw.setInt(3, fromAccountId);
            pstmtWithdraw.setDouble(4, amount);
            int rowsAffectedWithdraw = pstmtWithdraw.executeUpdate();
            if (rowsAffectedWithdraw > 0) {
                System.out.println("Withdrawn from source account successfully.");
                insertTransaction(fromAccountId, "Withdrawal", amount);
            } else {
                System.out.println("Failed to withdraw from source account. Transfer aborted.");
                conn.rollback();
                return;
            }


            //deposit to destination account
            String sqlDeposit = "UPDATE Accounts SET AccountBalance = AccountBalance + ? WHERE CustomerID = ? AND AccountID = ?";
            PreparedStatement pstmtDeposit = conn.prepareStatement(sqlDeposit);
            pstmtDeposit.setDouble(1, amount);
            pstmtDeposit.setInt(2, customerId);
            pstmtDeposit.setInt(3, toAccountId);
            int rowsAffectedDeposit = pstmtDeposit.executeUpdate();
            if (rowsAffectedDeposit > 0) {
                System.out.println("Deposited to destination account successfully.");
                insertTransaction(toAccountId, "Deposit", amount);
            } else {
                System.out.println("Failed to deposit to destination account. Transfer aborted.");
                conn.rollback();
                return;
            }

            //insert transaction for source account
            //insertTransaction(fromAccountId, "transfer", amount); //moved to above
            conn.commit();
            System.out.println("Transfer completed successfully.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback Error: " + rollbackEx.getMessage());
            }
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Auto-commit Reset Error: " + ex.getMessage());
            }
        }
    }
    
    public void payLoan(int loanId, int fromAccountId, int customerId, double amount) {
        try {
            conn.setAutoCommit(false);

            String sqlAccount = "UPDATE Accounts SET AccountBalance = AccountBalance - ? " +
                                "WHERE CustomerID = ? AND AccountID = ? AND AccountBalance >= ?";
            PreparedStatement pstmtAccount = conn.prepareStatement(sqlAccount);
            pstmtAccount.setDouble(1, amount);
            pstmtAccount.setInt(2, customerId);
            pstmtAccount.setInt(3, fromAccountId);
            pstmtAccount.setDouble(4, amount);
            int rowsAccount = pstmtAccount.executeUpdate();
            if (rowsAccount <= 0) {
                System.out.println("Failed to withdraw funds from account. Payment aborted.");
                conn.rollback();
                return;
            }

            String sqlLoan = "UPDATE Loans SET LoanAmount = LoanAmount - ? " + "WHERE LoanID = ? AND CustomerID = ?";
            PreparedStatement pstmtLoan = conn.prepareStatement(sqlLoan);
            pstmtLoan.setDouble(1, amount);
            pstmtLoan.setInt(2, loanId);
            pstmtLoan.setInt(3, customerId);
            int rowsLoan = pstmtLoan.executeUpdate();
            if (rowsLoan <= 0) {
                System.out.println("Failed to update loan balance. Payment aborted.");
                conn.rollback();
                return;
            }

            insertTransaction(fromAccountId, "LoanPayment", amount);
            conn.commit();
            System.out.println("Loan payment completed successfully.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback Error: " + rollbackEx.getMessage());
            }
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Auto-commit Reset Error: " + ex.getMessage());
            }
        }
    }


    public boolean validateBankerLogin(String username, String password) {
        try {
            System.out.println(username + password);
            String sql = "SELECT * FROM Bankers WHERE Username = ? AND Password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Banker login successful. Welcome, " + rs.getString("FirstName") + " " + rs.getString("LastName") + "!");
                return true;
            } else {
                System.out.println("Invalid banker credentials.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
    public Customer validateCustomerLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM Customers WHERE Username = ? AND Password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            Customer cust = null;
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Customer login successful. Welcome, " + rs.getString("FirstName") + " " + rs.getString("LastName") + "!");
                cust = new Customer(rs);
            } else {
                System.out.println("Invalid customer credentials.");
            }
            return cust;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return null;
        }
    }
    public ResultSet getCustomer(String username, String password) {
        try {
            String sql = "SELECT * FROM Customers WHERE Username = ? AND Password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return null;
        }

    }
}