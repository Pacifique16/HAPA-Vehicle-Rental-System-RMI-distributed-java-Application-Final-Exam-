# Banking System RMI Java

A distributed banking management system built with Java RMI (Remote Method Invocation), Hibernate ORM, and PostgreSQL database. This system enables tellers to manage customer accounts, process transactions, handle loans, and maintain risk profiles.

## Features

### Account Management
- Open new customer accounts with initial deposit
- Close existing accounts
- Check account balance
- View comprehensive account information (customer details, cards, loans, risk profile)
- View complete account transaction history

### Customer Management
- Register new customers with personal information
- Link customers to accounts
- Set and update customer risk profiles (LOW, MEDIUM, HIGH)

### Transaction Processing
- Deposit money into accounts
- Withdraw money from accounts
- Real-time balance updates
- Transaction history tracking

### Loan Management
- Request loans with customizable interest rates and periods
- Automatic calculation of total amount to pay and monthly deductions
- Loan payment processing with overpayment prevention
- Track loan status (INITIATED, PROGRESS, COMPLETED, REJECTED)
- View remaining loan balance after each payment

### Card Services
- Issue debit/credit cards (VISA, MASTERCARD)
- Block/deactivate cards
- Track card expiry dates

### Risk Assessment
- Assign risk profiles to customers
- Update risk profiles based on account activity

## Technology Stack

- **Java SE 8+**
- **Java RMI** - Distributed communication
- **Hibernate 4.3.1** - ORM framework
- **PostgreSQL** - Database
- **Swing** - GUI framework

## Architecture

The system follows a client-server architecture:

### Server Side (`BankingSystemRMIServer`)
- **Model Layer**: Entity classes (Account, Customer, Loan, Card, etc.)
- **DAO Layer**: Database access objects with Hibernate
- **Service Layer**: Business logic implementation
- **RMI Registry**: Exposes remote services on port 3000

### Client Side (`BankingSystemRMIClient`)
- **Model Layer**: Serializable entity classes
- **Service Layer**: Remote service interfaces
- **View Layer**: Swing-based GUI dialogs
- **RMI Client**: Connects to server services

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- PostgreSQL 9.4 or higher
- NetBeans IDE (recommended) or any Java IDE
- PostgreSQL JDBC Driver
- Hibernate libraries

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE java_thursday_2025;
```

2. Copy `hibernate.cfg.xml.example` to `hibernate.cfg.xml` and update credentials:
```bash
cd BankingSystemRMIServer/src
cp hibernate.cfg.xml.example hibernate.cfg.xml
```

Then edit `hibernate.cfg.xml` with your database credentials:
```xml
<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/java_thursday_2025</property>
<property name="hibernate.connection.username">your_username</property>
<property name="hibernate.connection.password">your_password</property>
```

3. Tables will be auto-generated on first run using Hibernate's `hbm2ddl.auto=update`

## Installation & Running

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/banking-system-rmi-java.git
cd banking-system-rmi-java
```

### 2. Start the Server
```bash
cd BankingSystemRMIServer
# Build and run using your IDE or:
javac -cp "lib/*:src" src/controller/Server.java
java -cp "lib/*:src" controller.Server
```

Server will start on port 3000 and display: `Server is running on port 3000`

### 3. Start the Client
```bash
cd BankingSystemRMIClient
# Build and run using your IDE or:
javac -cp "lib/*:src" src/view/TellerDeskView.java
java -cp "lib/*:src" view.TellerDeskView
```

## Usage Guide

### Opening a New Account
1. Click **OPEN NEW ACCOUNT**
2. Fill in customer information (name, email, phone, address, DOB, gender)
3. Enter account number and initial deposit
4. Select account type (SAVING, CURRENT, FIXED_ACCOUNT)
5. Click **OPEN ACCOUNT**

### Processing Transactions
**Deposit:**
1. Click **DEPOSIT MONEY**
2. Enter account number and amount
3. Click **DEPOSIT**

**Withdraw:**
1. Click **WITHDRAW MONEY**
2. Enter account number and amount
3. Click **WITHDRAW**

### Loan Management
**Request Loan:**
1. Click **LOAN REQUEST**
2. Enter account number, loan amount, interest rate, and period (months)
3. Click **APPROVE LOAN**

**Make Payment:**
1. Click **LOAN PAYMENT**
2. Enter account number and click **Search Loan**
3. View loan details and remaining balance
4. Enter payment amount (cannot exceed remaining balance)
5. Click **RECORD PAYMENT**

### Viewing Account Information
1. Click **ACCOUNT INFO**
2. Enter account number and click **Search**
3. View complete details: customer info, cards, loans with remaining balances

### Setting Risk Profile
1. Click **RISK PROFILE**
2. Enter account number and click **Search**
3. Select risk type (LOW, MEDIUM, HIGH)
4. Click **Set Risk Profile**

## Database Schema

### Main Tables
- `account` - Account information
- `customer` - Customer personal details
- `card` - Card information
- `loan` - Loan details
- `loan_repayment` - Loan payment records
- `transaction` - Transaction history
- `risk_profile` - Customer risk assessments

### Join Tables
- `customer_has_account` - Links customers to accounts
- `loan_registered_to` - Links loans to accounts

## Key Features Implementation

### Lazy Loading Prevention
The system uses `Hibernate.initialize()` to eagerly load related entities and prevent lazy initialization exceptions when data is transferred via RMI.

### Overpayment Prevention
Both client and server validate loan payments to ensure they don't exceed the remaining loan balance.

### Transaction Tracking
All deposits, withdrawals, loan disbursements, and repayments are tracked in the account history.

## Project Structure

```
26937_Pacifique_HARERIMANA/
├── BankingSystemRMIServer/
│   ├── src/
│   │   ├── controller/
│   │   │   └── Server.java
│   │   ├── dao/
│   │   │   ├── AccountDao.java
│   │   │   ├── LoanDao.java
│   │   │   └── ...
│   │   ├── model/
│   │   │   ├── Account.java
│   │   │   ├── Customer.java
│   │   │   └── ...
│   │   ├── service/
│   │   │   └── implementation/
│   │   └── hibernate.cfg.xml
│   └── lib/
└── BankingSystemRMIClient/
    ├── src/
    │   ├── model/
    │   ├── service/
    │   └── view/
    │       ├── TellerDeskView.java
    │       ├── AccountInfoDialog.java
    │       └── ...
    └── lib/
```

## Troubleshooting

### Port Already in Use
If you get "Port already in use: 3000" error:
```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :3000
kill -9 <PID>
```

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials in `hibernate.cfg.xml`
- Ensure PostgreSQL JDBC driver is in classpath

### RMI Connection Failed
- Ensure server is running before starting client
- Check firewall settings for port 3000
- Verify `127.0.0.1` is accessible

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Pacifique HARERIMANA**
- Student ID: 26937
- Institution: AUCA (Adventist University of Central Africa)
- Course: Java Programming - Semester 7

## Acknowledgments

- Java RMI documentation
- Hibernate ORM framework
- PostgreSQL database
- NetBeans IDE community
