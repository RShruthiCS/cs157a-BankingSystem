CREATE TABLE IF NOT EXISTS Customers (
    CustomerID SERIAL PRIMARY KEY, 
    Username VARCHAR(50) UNIQUE NOT NULL, 
    Password VARCHAR(50) NOT NULL, 
    FirstName VARCHAR(50) NOT NULL, 
    LastName VARCHAR(50) NOT NULL, 
    Email VARCHAR(100) UNIQUE NOT NULL, 
    PhoneNumber VARCHAR(20), 
    MailingAddress VARCHAR(200), 
    DateOfBirth DATE, 
    SSN VARCHAR(11) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Accounts ( 
    AccountID SERIAL PRIMARY KEY, 
    CustomerID INT REFERENCES Customers(CustomerID), 
    AccountBalance DECIMAL(15, 2) DEFAULT 0.00 CHECK (AccountBalance >= 0), 
    AccountType VARCHAR(50) CHECK (AccountType IN ('Checkings','Savings')), 
    DateOpened DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS Transactions ( 
    TransactionID SERIAL PRIMARY KEY, 
    AccountID INT REFERENCES Accounts(AccountID), 
    TransactionType VARCHAR(50) CHECK (TransactionType IN ('Deposit','Withdrawal','Transfer')), 
    Amount DECIMAL(15, 2) NOT NULL, 
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Loans ( 
    LoanID SERIAL PRIMARY KEY, 
    CustomerID INT REFERENCES Customers(CustomerID), 
    LoanAmount DECIMAL(15, 2) NOT NULL, 
    InterestRate DECIMAL(5, 2) NOT NULL, 
    Status VARCHAR(20) CHECK (Status IN ('Pending','Approved','Rejected','Paid')) DEFAULT 'Pending', 
    StartDate DATE DEFAULT CURRENT_DATE, 
    EndDate DATE
);

CREATE TABLE IF NOT EXISTS Bankers ( 
    BankerID SERIAL PRIMARY KEY, 
    FirstName VARCHAR(50) NOT NULL, 
    LastName VARCHAR(50) NOT NULL, 
    Title VARCHAR(50), 
    Username VARCHAR(50) UNIQUE NOT NULL, 
    Password VARCHAR(50) NOT NULL, 
    Email VARCHAR(100) UNIQUE NOT NULL, 
    PhoneNumber VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Beneficiaries ( 
    BeneficiaryID SERIAL PRIMARY KEY, 
    CustomerID INT REFERENCES Customers(CustomerID), 
    FirstName VARCHAR(50) NOT NULL, 
    LastName VARCHAR(50) NOT NULL, 
    Relationship VARCHAR(50), 
    MailingAddress VARCHAR(100) NOT NULL, 
    PhoneNumber VARCHAR(20)
);