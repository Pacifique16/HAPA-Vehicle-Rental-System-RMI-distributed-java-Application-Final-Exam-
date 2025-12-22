# 🚗 HAPA Vehicle Rental System

> A comprehensive distributed Java RMI application for modern vehicle rental management with enterprise-grade features.

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.7-blue.svg)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-5.6.15-green.svg)](https://hibernate.org/)
[![ActiveMQ](https://img.shields.io/badge/ActiveMQ-5.18.3-red.svg)](https://activemq.apache.org/)

## 🌟 Key Features

### 👥 **User Management**
- Secure user registration with strong password validation
- Role-based access control (Admin/Customer)
- Profile management and account status tracking
- Email-based OTP authentication

### 🚙 **Vehicle Management**
- Comprehensive vehicle inventory system
- Real-time availability tracking
- Multiple vehicle categories (SUV, Sedan, Van, Luxury, etc.)
- Vehicle maintenance records

### 📋 **Booking System**
- Intuitive vehicle reservation interface
- Admin approval workflow
- Booking status tracking (Pending, Approved, Rejected, Cancelled)
- Automated pricing calculations

### 🔐 **Security & Authentication**
- Session management with automatic timeout
- OTP verification via email
- Secure password requirements
- IP tracking and audit trails

### 📊 **Reports & Analytics**
- Real-time dashboard with key metrics
- PDF and CSV export functionality
- Booking history and analytics
- Vehicle utilization reports

### 🔄 **Messaging System**
- ActiveMQ message broker integration
- Asynchronous email processing
- Reliable OTP delivery system

## 🏗️ System Architecture

```
┌─────────────────┐    RMI     ┌─────────────────┐
│   Swing Client  │ ◄────────► │   RMI Server    │
│                 │            │                 │
│ • Admin GUI     │            │ • User Service  │
│ • Customer GUI  │            │ • Vehicle Svc   │
│ • Reports       │            │ • Booking Svc   │
└─────────────────┘            │ • OTP Service   │
                               └─────────────────┘
                                        │
                               ┌─────────────────┐
                               │   PostgreSQL    │
                               │    Database     │
                               └─────────────────┘
                                        │
                               ┌─────────────────┐
                               │   ActiveMQ      │
                               │   Broker        │
                               └─────────────────┘
```

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|----------|
| **Backend** | Java | 8+ |
| **ORM** | Hibernate | 5.6.15 |
| **Database** | PostgreSQL | 42.7.7 |
| **Messaging** | ActiveMQ | 5.18.3 |
| **PDF Export** | iText | 5.5.13 |
| **GUI** | Java Swing | Built-in |
| **Communication** | Java RMI | Built-in |

## 🚀 Quick Start Guide

### Prerequisites
- ☕ Java 8 or higher
- 🐘 PostgreSQL 12+
- 📧 Gmail account for OTP emails

### 1. Database Setup
```bash
# Create database
psql -U postgres
CREATE DATABASE hapa_vehicle_rental_db;
\c hapa_vehicle_rental_db;

# Run sample data script (after first server start)
\i database_setup.sql
```

### 2. Start Server
```bash
cd HAPAVehicleRentalServer26937/src
java controller.Server
```
**Expected Output:**
```
✅ Embedded ActiveMQ Broker started on tcp://localhost:61616
✅ HAPA Vehicle Rental Server is running on port 3506
```

### 3. Launch Client
```bash
cd HapaVehicleRentalClient26937/src
java view.LoginForm
```

## 🔑 Default Credentials

### Admin Access
- **Username:** `admin`
- **Password:** `Admin123!`
- **Features:** Full system access, user management, vehicle management, booking approvals

### Customer Registration
- Create new account with strong password
- **Requirements:** Min 8 chars, uppercase, lowercase, number, special character
- **Example:** `MyPassword123!`

## 📁 Project Structure

```
HAPA-Vehicle-Rental-System/
├── 🖥️ HAPAVehicleRentalServer26937/     # Backend RMI Services
│   ├── src/
│   │   ├── controller/                   # Server startup
│   │   ├── dao/                         # Data Access Layer
│   │   ├── model/                       # Entity classes
│   │   ├── service/                     # Business logic
│   │   └── util/                        # Utilities
│   └── lib/                             # Server dependencies
├── 🖼️ HapaVehicleRentalClient26937/      # Frontend GUI Application
│   ├── src/
│   │   ├── model/                       # Client-side models
│   │   ├── service/                     # RMI interfaces
│   │   ├── util/                        # Client utilities
│   │   └── view/                        # Swing GUI components
│   ├── lib/                             # Client dependencies
│   └── images/                          # Vehicle images
├── 📄 README.md                         # This file
├── 🧪 TESTING_GUIDE.md                  # Testing instructions
├── 🗃️ DATABASE_SETUP.md                 # Database configuration
└── 📊 database_setup.sql                # Sample data
```

## 🎯 Core Functionalities

### Admin Dashboard
- 📊 **Analytics Overview:** Real-time statistics and metrics
- 👥 **User Management:** Add, edit, delete, and manage user accounts
- 🚗 **Vehicle Management:** Complete vehicle inventory control
- ✅ **Booking Approvals:** Review and approve/reject reservations
- 📈 **Reports:** Generate PDF/CSV reports with filtering

### Customer Portal
- 🔍 **Vehicle Browse:** Search and filter available vehicles
- 📅 **Booking System:** Reserve vehicles with date selection
- 📋 **My Bookings:** Track reservation status and history
- 👤 **Profile Management:** Update personal information
- 📄 **Export Data:** Download booking history as PDF/CSV

## 🔧 Configuration

### Database Connection
Update `hibernate.cfg.xml` in server project:
```xml
<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/hapa_vehicle_rental_db</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">your_password</property>
```

### Email Configuration
Update `EmailConfig.java` for OTP emails:
```java
public static final String SMTP_HOST = "smtp.gmail.com";
public static final String EMAIL_USERNAME = "your-email@gmail.com";
public static final String EMAIL_PASSWORD = "your-app-password";
```

## 🧪 Testing

See [TESTING_GUIDE.md](TESTING_GUIDE.md) for comprehensive testing instructions.

## 📚 Documentation

- [🗃️ Database Setup Guide](DATABASE_SETUP.md)
- [🧪 Testing Guide](TESTING_GUIDE.md)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Pacifique HARERIMANA**
- Email: harerimanapacifique95@gmail.com
- Student ID: 26937
- Institution: AUCA (Adventist University of Central Africa)

## 🎯 Project Purpose

This project was developed as a **Final Exam Project** for the **Java Programming** course, demonstrating:

- **Java RMI** implementation for distributed computing
- **Enterprise application architecture** with proper separation of concerns
- **Database integration** using Hibernate ORM
- **Message-oriented middleware** with ActiveMQ
- **Security implementation** with session management and OTP authentication
- **Modern GUI development** with Java Swing
- **Report generation** and data export capabilities

The system showcases advanced Java enterprise development skills and distributed system design patterns.

---

**Built with ❤️ for modern vehicle rental management**