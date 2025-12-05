-- NUKE EVERYTHING FIRST
TRUNCATE TABLE Customers, Accounts, Transactions, Loans, Bankers, Beneficiaries RESTART IDENTITY CASCADE;

-- CUSTOMERS TABLE INITIALIZATION
-- This table must be initialized first due to foreign key dependencies in other tables
INSERT INTO Customers(Username, Password, FirstName, LastName, Email, PhoneNumber, MailingAddress, DateOfBirth, SSN) 
VALUES 
--1
('jdoe', 'password123', 'John', 'Doe', 'jdoe@example.com', '555-1234', '123 Main St, Anytown, USA', '1980-01-01', '123-45-6789'),
--2
('asmith', 'securepass', 'Alice', 'Smith', 'asmith@example.com', '555-5678', '456 Oak St, Othertown, USA', '1990-05-15', '987-65-4321'),
--3
('bwilliams', 'mypassword', 'Bob', 'Williams', 'billie@example.com', '555-8765', '789 Pine St, Sometown, USA', '1975-09-30', '111-22-3333'),
--4
('cjones', 'passw0rd', 'Carol', 'Jones', 'cajon@example.com', '555-4321', '321 Maple St, Anycity, USA', '1985-12-25', '444-55-6666'),
--5
('djohnson', 'letmein', 'David', 'Johnson', 'johnd@example.com', '555-6789', '654 Cedar St, Othercity, USA', '1992-07-04', '777-88-9999'),
--6
('emartin', 'qwerty', 'Eva', 'Martin', 'martianman@example.com', '555-3456', '987 Birch St, Newtown, USA', '1988-03-14', '222-33-4444'),
--7
('fthomas', 'abc123', 'Frank', 'Thomas', 'fomas@example.com', '555-7890', '159 Spruce St, Oldtown, USA', '1979-11-11', '555-66-7777'),
--8
('ggarcia', 'zxcvbn', 'Grace', 'Garcia', 'gragar@example.com', '555-2345', '753 Willow St, Smalltown, USA', '1995-02-28', '888-99-0000'),
--9
('hrodriguez', 'pass1234', 'Hank', 'Rodriguez', 'hork@example.com', '555-8901', '852 Aspen St, Bigtown, USA', '1983-06-18', '333-44-5555'),
--10
('imiller', 'welcome1', 'Ivy', 'Miller', 'millie@example.com', '555-0123', '951 Chestnut St, Littletown, USA', '1991-10-09', '666-77-8888'),
--11
('jwilson', 'letmein123', 'Jack', 'Wilson', 'winningjack@example.com', '555-4567', '357 Poplar St, Middletown, USA', '1987-04-22', '999-00-1111'),
--12
('kmoore', 'password1', 'Kathy', 'Moore', 'morekathyplz@example.com', '555-6780', '258 Fir St, Uptown, USA', '1993-08-05', '121-21-2121'),
--13
('lwhite', '12345678', 'Larry', 'White', 'lwhiter@example.com', '555-8902', '147 Hemlock St, Downtown, USA', '1982-03-03', '232-32-3232'),
--14
('msanchez', 'iloveyou', 'Maria', 'Sanchez', 'mamacita@example.com', '555-3457', '369 Magnolia St, Suburbia, USA', '1994-12-12', '343-43-4343'),
--15
('nlee', 'trustno1', 'Nathan', 'Lee', 'trustnoone@example.com', '555-5670', '741 Dogwood St, Countryside, USA', '1986-09-09', '454-54-5454');

-- ACCOUNTS TABLE INITIALIZATION
-- This table must be initialized second because Transactions depend on Accounts
INSERT INTO Accounts(CustomerID, AccountBalance, AccountType, DateOpened)
VALUES 
--1
(1, 1000.00, 'Checkings', '2020-01-15'),
--2
(1, 2500.50, 'Savings', '2019-11-20'),
--3
(2, 1500.75, 'Checkings', '2021-06-10'),
--4
(3, 3000.00, 'Savings', '2018-03-05'),
--5
(4, 500.25, 'Checkings', '2022-09-12'),
--6
(5, 750.00, 'Savings', '2020-12-01'),
--7
(6, 1200.00, 'Checkings', '2019-07-23'),
--8
(7, 2000.00, 'Savings', '2021-04-18'),
--9
(8, 1800.50, 'Checkings', '2022-02-14'),
--10
(9, 2200.75, 'Savings', '2018-10-30'),
--11
(10, 1600.00, 'Checkings', '2020-05-22'),
--12
(11, 1400.25, 'Savings', '2019-08-09'),
--13
(12, 900.00, 'Checkings', '2021-11-11'),
--14
(13, 2750.00, 'Savings', '2017-12-31'),
--15
(14, 1300.50, 'Checkings', '2022-06-06');

-- TRANSACTIONS TABLE INITIALIZATION
INSERT INTO Transactions(AccountID, TransactionType, Amount, TransactionDate)
VALUES 
--1
(1, 'Deposit', 500.00, '2020-01-16 10:00:00'),
--2
(1, 'Withdrawal', 200.00, '2020-01-20 14:30:00'),
--3
(2, 'Deposit', 1000.00, '2019-11-21 09:15:00'),
--4
(2, 'Withdrawal', 300.00, '2019-11-25 16:45:00'),
--5
(3, 'Deposit', 750.00, '2021-06-11 11:20:00'),
--6
(3, 'Withdrawal', 100.00, '2021-06-15 13:50:00'),
--7
(4, 'Deposit', 1200.00, '2018-03-06 08:30:00'),
--8
(4, 'Withdrawal', 400.00, '2018-03-10 15:00:00'),
--9
(5, 'Deposit', 250.00, '2022-09-13 12:10:00'),
--10
(5, 'Withdrawal', 50.00, '2022-09-15 17:25:00'),
--11
(6, 'Deposit', 300.00, '2020-12-02 10:40:00'),
--12
(6, 'Withdrawal', 100.00, '2020-12-05 14:55:00'),
--13
(7, 'Deposit', 600.00, '2019-07-24 09:05:00'),
--14
(7, 'Withdrawal', 150.00, '2019-07-28 16:15:00'),
--15
(8, 'Deposit', 800.00, '2021-04-19 11:30:00');

-- LOANS TABLE INITIALIZATION
INSERT INTO Loans(CustomerID, LoanAmount, InterestRate, Status, StartDate, EndDate)
VALUES 
--1
(1, 5000.00, 0.05, 'Approved', '2020-02-01', '2025-02-01'),
--2
(2, 10000.00, 0.05, 'Pending', '2021-07-15', NULL),
--3
(3, 7500.00, 0.01, 'Rejected', '2019-04-10', NULL),
--4
(4, 12000.00, 0.03, 'Approved', '2022-10-20', '2027-10-20'),
--5
(5, 3000.00, 0.04, 'Paid', '2018-05-05', '2021-05-05'),
--6
(6, 15000.00, 0.06, 'Approved', '2019-08-30', '2024-08-30'),
--7
(7, 2000.00, 0.02, 'Pending', '2021-03-12', NULL),
--8
(8, 8000.00, 0.05, 'Rejected', '2022-01-25', NULL),
--9
(9, 6000.00, 0.03, 'Approved', '2018-11-11', '2023-11-11'),
--10
(10, 4000.00, 0.04, 'Paid', '2020-06-18', '2023-06-18'),
--11
(11, 11000.00, 0.05, 'Approved', '2019-09-09', '2024-09-09'),
--12
(12, 9000.00, 0.02, 'Pending', '2021-12-01', NULL),
--13
(13, 7000.00, 0.03, 'Rejected', '2017-10-31', NULL),
--14
(14, 13000.00, 0.06, 'Approved', '2022-07-07', '2027-07-07'),
--15
(15, 2500.00, 0.04, 'Paid', '2018-03-15', '2021-03-15');

-- BENEFICIARIES TABLE INITIALIZATION
INSERT INTO Beneficiaries(CustomerID, FirstName, LastName, Relationship, MailingAddress, PhoneNumber)
VALUES 
--1
(1, 'Jane', 'Doe', 'Spouse', '123 Main St, Anytown, USA', '555-4321'),
--2
(2, 'Mark', 'Smith', 'Brother', '456 Oak St, Othertown, USA', '555-8765'),
--3
(3, 'Linda', 'Williams', 'Mother', '789 Pine St, Sometown, USA', '555-6543'),
--4
(4, 'James', 'Jones', 'Father', '321 Maple St, Anycity, USA', '555-2109'),
--5
(5, 'Susan', 'Johnson', 'Sister', '654 Cedar St, Othercity, USA', '555-3450'),
--6
(6, 'Robert', 'Martin', 'Friend', '987 Birch St, Newtown, USA', '555-7891'),
--7
(7, 'Patricia', 'Thomas', 'Spouse', '159 Spruce St, Oldtown, USA', '555-0124'),
--8
(8, 'Michael', 'Garcia', 'Brother', '753 Willow St, Smalltown, USA', '555-4568'),
--9
(9, 'Elizabeth', 'Rodriguez', 'Mother', '852 Aspen St, Bigtown, USA', '555-8903'),
--10
(10, 'William', 'Miller', 'Father', '951 Chestnut St, Littletown, USA', '555-1235'),
--11
(11, 'Barbara', 'Wilson', 'Sister', '357 Poplar St, Middletown, USA', '555-6781'),
--12
(12, 'Charles', 'Moore', 'Friend', '258 Fir St, Uptown, USA', '555-8904'),
--13
(13, 'Jennifer', 'White', 'Spouse', '147 Hemlock St, Downtown, USA', '555-2346'),
--14
(14, 'Daniel', 'Sanchez', 'Brother', '369 Magnolia St, Suburbia, USA', '555-5671'),
--15
(15, 'Sarah', 'Lee', 'Mother', '741 Dogwood St, Countryside, USA', '555-7892');

-- BANKERS TABLE INITIALIZATION
INSERT INTO Bankers(FirstName, LastName, Title, Username, Password, Email, PhoneNumber)
VALUES 
--1
('Michael', 'Brown', 'Senior Banker', 'mbrown', 'bankerpass', 'mmmbrownies@example.com', '555-1111'),
--2
('Linda', 'Davis', 'Junior Banker', 'ldavis', 'securebank', 'dalinda@example.com', '555-2222'),
--3
('James', 'Wilson', 'Account Manager', 'jwilson', 'manageme', 'jamiewilli@example.com', '555-3333'),
--4
('Patricia', 'Taylor', 'Loan Officer', 'ptaylor', 'loan4u', 'patrick@example.com', '555-4444'),
--5
('Robert', 'Anderson', 'Customer Service', 'randerson', 'helpme', 'bobbyson@example.com', '555-5555'),
--6
('Jennifer', 'Thomas', 'Financial Advisor', 'jthomas', 'advisor123', 'jennie@example.com', '555-6666'),
--7
('William', 'Jackson', 'Branch Manager', 'wjackson', 'branchman', 'williejack@example.com', '555-7777'),
--8
('Elizabeth', 'White', 'Teller', 'ewhite', 'tellertalk', 'whiteeli@example.com', '555-8888'),
--9
('David', 'Harris', 'Compliance Officer', 'dharris', 'compliance', 'harrisonford@example.com', '555-9999'),
--10
('Susan', 'Martin', 'Risk Analyst', 'smartin', 'riskitall', 'carbar@example.com', '555-0000'),
--11
('Charles', 'Garcia', 'Investment Banker', 'cgarcia', 'invest123', 'charlia@example.com', '555-1212'),
--12
('Jessica', 'Martinez', 'Credit Analyst', 'jmartinez', 'creditme', 'martijessie@example.com', '555-3434'),
--13
('Daniel', 'Robinson', 'Operations Manager', 'drobinson', 'operate', 'dannyrobyou@example.com', '555-5656'),
--14
('Sarah', 'Clark', 'Marketing Specialist', 'sclark', 'marketit', 'clarkkent@example.com', '555-7878'),
--15
('Matthew', 'Rodriguez', 'IT Specialist', 'mrodriguez', 'itsme', 'matatack@example.com', '555-9090');

