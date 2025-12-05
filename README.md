# Banking Satabase Sanagement System

/SJSU/CS157A-IntroToDBMS/

Contributors:   Bryan Morales       | *Backend, DBMS*
                Shruthi Raghavan    | *Frontend*
                Taras Tishchenko    | *Backend, DBMS, QA*

## Components
    IDE:                VSCode
    DBMS:               PostgreSQL
    Containerization:   Docker
    GUI:                Swing

## Folder Structure

- `src`:        the folder to maintain sources
- `lib`:        the folder to maintain dependencies
- `java`:       the folder for Java functions
- `resources`:  the folder for SQL schema & prepopulated table data

## Getting Started
1. VSCode Setup
        1.1. Download VSCode software @ https://code.visualstudio.com/
        1.2. Install the following extensions:
            1.2.1. Extension Pack for Java
            1.2.2. Language Support for Java(TM) by Red Hat
            1.2.3. Maven for Java
            1.2.4. Project Manager for Java
            1.2.5. Test Runner for Java
            1.2.6. SQLTools
            1.2.7. SQLTools PostgreSQL/Cockroach Driver
            1.2.8. PostgreSQL
            1.2.9. Docker
        1.3. Open JDBCPROJECT folder in IDE
2. Docker Setup
        2.1. Download Docker software @ https://www.docker.com/
        2.2. Install & run the program.
        2.3. In Docker extension in VSCode, perform the following steps:
            2.3.1. Create a new server
            2.3.2. Server name:                 localhost
            2.3.3. Authentication Type          Password
            2.3.4. Username                     postgres
            2.3.5. Password                     postgres-Dockercs157a
            2.3.6. Connection Name              cs157a-BankingSystem
            2.3.7. Server Group                 servers
            2.3.8. Advanced/Port                54876
            2.3.9. Advanced/Application Name    vscode-pgsql
            2.3.10. [ Save & Connect ]
        2.4. Create a new database 
            2.4.1. [ New Query ]
            2.4.2.  -- Create the schema
                    CREATE SCHEMA IF NOT EXISTS banking;

                    -- Customers Table
                    CREATE TABLE banking.Customers (
                        CustomerID SERIAL PRIMARY KEY,
                        Username VARCHAR(50) UNIQUE NOT NULL,
                        Password VARCHAR(255) NOT NULL,
                        Email VARCHAR(100) UNIQUE NOT NULL,
                        FirstName VARCHAR(50) NOT NULL,
                        LastName VARCHAR(50) NOT NULL,
                        BirthDate DATE NOT NULL,
                        BailingAddress VARCHAR(255),
                        PhoneNumber VARCHAR(20),
                        SocialSecNumber VARCHAR(15) UNIQUE NOT NULL
                    );

                    -- Accounts Table
                    CREATE TABLE banking.Accounts (
                        AccountID SERIAL PRIMARY KEY,
                        CustomerID INT NOT NULL,
                        AccountBalance NUMERIC(15, 2) NOT NULL,
                        AccountStatus VARCHAR(20) NOT NULL,
                        AccountType VARCHAR(25) NOT NULL,
                        FOREIGN KEY (CustomerID) REFERENCES banking.Customers(CustomerID)
                    );

                    -- Transactions Table
                    CREATE TABLE banking.Transactions (
                        TransactionID SERIAL PRIMARY KEY,
                        AccountID INT NOT NULL,
                        TransactionAmount NUMERIC(15, 2) NOT NULL,
                        TransactionDate TIMESTAMP NOT NULL,
                        TransactionType VARCHAR(30) NOT NULL,
                        TransactionDescription TEXT,
                        FOREIGN KEY (AccountID) REFERENCES banking.Accounts(AccountID)
                    );

                    -- Beneficiaries Table
                    CREATE TABLE banking.Beneficiaries (
                        BeneficiaryID SERIAL PRIMARY KEY,
                        CustomerID INT NOT NULL,
                        Firstname VARCHAR(50) NOT NULL,
                        Lastname VARCHAR(50) NOT NULL,
                        Email VARCHAR(100),
                        MailingAddress VARCHAR(255),
                        PhoneNumber VARCHAR(20),
                        FOREIGN KEY (CustomerID) REFERENCES banking.Customers(CustomerID)
                    );

                    -- Loans Table
                    CREATE TABLE banking.Loans (
                        LoanID SERIAL PRIMARY KEY,
                        CustomerID INT NOT NULL,
                        LoanStatus VARCHAR(30) NOT NULL,
                        LoanAmount NUMERIC(15, 2) NOT NULL,
                        LoanType VARCHAR(35) NOT NULL,
                        LoanInterest NUMERIC(5, 2) NOT NULL,
                        LoanStartDate DATE NOT NULL,
                        LoanEndDate DATE,
                        FOREIGN KEY (CustomerID) REFERENCES banking.Customers(CustomerID)
                    );

                    -- Bankers Table
                    CREATE TABLE banking.Bankers (
                        BankerID SERIAL PRIMARY KEY,
                        Firstname VARCHAR(50) NOT NULL,
                        Lastname VARCHAR(50) NOT NULL,
                        Title VARCHAR(50) NOT NULL,
                        Username VARCHAR(50) UNIQUE NOT NULL,
                        Password VARCHAR(255) NOT NULL,
                        Email VARCHAR(100) UNIQUE NOT NULL,
                        PhoneNumber VARCHAR(20)
                    );
3. SQLTools Setup
        3.1. Add new connection
            3.1.1. Connection name*     cs157a-BankingSystem
            3.1.2. Connect using*       Server and Port
            3.1.3. Server Address*      localhost
            3.1.4. Port*                54876
            3.1.5. Database*            postgres
            3.1.6. Username*            postgres
            3.1.7. Use password         Save as plaintext in settings
            3.1.8. Password*            postgres-Dockercs157a
            3.1.9. Connection Timeout   15
            3.1.10. [ Test Connection ] & [ Save Connection ]
4. JDBC Driver Setup
        4.1. Download PostgreSQL JDBC driver @ https://jdbc.postgresql.org/
        4.2. Add downloaded .jar file to path: [/JDBCPROJECT/JDBCProject/lib/](lib)
        4.3. Ensure the file is in Referenced Libraries
        4.4. Ensure '[resources](src/main/resources)' folder is in Java Source Path & it contains the followinf files:
            - [create_schema.sql](src/main/resources/create_schema.sql)
            - [initialize_data.sql](src/main/resources/initialize_data.sql)
        4.5. Ensure .sql files are connected to localhost
5. RUN [Main.java](src/main/java/Main.java)
        5.1. Ensure mathing values:
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:54876/bankingsystem", "postgres", "postgres-Dockercs157a")) 
6. RUN [BankingSystemGUI.java](src/main/java/BankingSystemGUI.java)
        6.1. Ensure matching values:
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:54876/bankingsystem";
            String user = "postgres";
            String password = "postgres-Dockercs157a";

## Dependencies
    * PostgreSQL JDBC Library
    * Docker 
    * javax.swing