# HAPA Vehicle Rental System - Final Exam Project
**Student ID: 26937**  
**Student Name: Pacifique HARERIMANA**

## Project Overview
This is a distributed Java application for vehicle rental management system built using RMI (Remote Method Invocation), Hibernate ORM, and Swing GUI. The system follows MVC and DAO design patterns as required.

## Architecture
- **Client Application**: HAPAVehicleRentalClient26937
- **Server Application**: HAPAVehicleRentalServer26937
- **Communication**: RMI on port 3500 (within required range 3000-4000)
- **Database**: PostgreSQL (hapa_vehicle_rental_db)
- **ORM**: Hibernate Framework

## Entity Relationships Implemented
1. **One-to-One**: User ↔ UserProfile
2. **One-to-Many**: User → Booking (one user can have many bookings)
3. **Many-to-Many**: Vehicle ↔ Maintenance (vehicles can have multiple maintenance records)

## Business Validation Rules (5 implemented)
1. Username must be unique during registration
2. Cannot delete admin if it's the last admin user
3. Cannot update/delete vehicle that is currently rented
4. Maximum rental period is 30 days
5. Start date must be in the future for new bookings

## Technical Validation Rules (5 implemented)
1. Email format validation using regex
2. Phone number format validation
3. Password must be at least 6 characters long
4. Price per day must be greater than 0
5. Number of seats must be between 1 and 50

## Project Structure

### Server Side (HAPAVehicleRentalServer26937)
```
src/
├── model/           # JPA entities with annotations
│   ├── User.java
│   ├── Vehicle.java
│   ├── Booking.java
│   ├── UserProfile.java
│   └── Maintenance.java
├── dao/             # Data Access Objects
│   ├── HibernateUtil.java
│   ├── UserDao.java
│   ├── VehicleDao.java
│   ├── BookingDao.java
│   └── MaintenanceDao.java
├── service/         # Remote interfaces
│   ├── UserService.java
│   ├── VehicleService.java
│   └── BookingService.java
├── service/implementation/  # Service implementations
│   ├── UserServiceImpl.java
│   ├── VehicleServiceImpl.java
│   └── BookingServiceImpl.java
├── controller/      # Server controller (SKELETON)
│   └── Server.java
└── hibernate.cfg.xml
```

### Client Side (HAPAVehicleRentalClient26937)
```
src/
├── model/           # POJOs without JPA annotations
│   ├── User.java
│   ├── Vehicle.java
│   └── Booking.java
├── service/         # Remote interfaces (same as server)
│   ├── UserService.java
│   ├── VehicleService.java
│   └── BookingService.java
└── view/            # Swing GUI components (STUB)
    ├── LoginForm.java
    ├── RegistrationForm.java
    ├── AdminDashboard.java
    ├── CustomerDashboard.java
    ├── VehicleForm.java
    └── BookingForm.java
```

## Setup Instructions

### 1. Database Setup
1. Install PostgreSQL
2. Create database: `hapa_vehicle_rental_db`
3. Run the SQL script: `database_setup.sql`
4. Update database credentials in `hibernate.cfg.xml`

### 2. Required Libraries
Add these JAR files to both projects:
- Hibernate Core
- Hibernate JPA
- PostgreSQL JDBC Driver
- SLF4J (for logging)

### 3. Running the Application
1. **Start Server**: Run `Server.java` in HAPAVehicleRentalServer26937
2. **Start Client**: Run `LoginForm.java` in HAPAVehicleRentalClient26937

### 4. Default Login Credentials
- **Admin**: username=`admin`, password=`admin123`
- **Customer**: username=`john_doe`, password=`password123`

## Features Implemented

### Core Requirements ✅
- [x] Distributed Java application using RMI
- [x] Server accepts connections on port 3500 (3000-4000 range)
- [x] MVC and DAO design patterns
- [x] Hibernate framework for ORM
- [x] All three entity relationships (1:1, 1:M, M:M)
- [x] CRUD operations with JTables for data display
- [x] 5 business validation rules
- [x] 5 technical validation rules
- [x] JOptionPane for user messages
- [x] Swing GUI with multiple forms

### GUI Components
- **LoginForm**: User authentication
- **RegistrationForm**: New user registration
- **AdminDashboard**: Vehicle, booking, and user management
- **CustomerDashboard**: Vehicle browsing and booking management
- **VehicleForm**: Add/edit vehicles
- **BookingForm**: Create new bookings

### Additional Features to Implement
- [ ] PDF/Excel/CSV report generation
- [ ] Real-time notifications (ActiveMQ/RabbitMQ)
- [ ] OTP for login via email/phone
- [ ] Session management across applications

## RMI Communication Flow
1. **Client (STUB)**: Connects to server via RMI registry
2. **Server (SKELETON)**: Accepts requests and processes them
3. **Services**: Handle business logic and validation
4. **DAOs**: Interact with database via Hibernate
5. **Response**: Sent back to client through RMI

## Validation Examples
- **Business**: "Cannot delete the last admin user"
- **Technical**: "Email format validation using regex pattern"

## Database Schema
- **users**: User accounts and authentication
- **user_profiles**: Extended user information (1:1 with users)
- **vehicles**: Vehicle inventory
- **bookings**: Rental transactions (M:1 with users and vehicles)
- **maintenance**: Maintenance records
- **vehicle_maintenance**: Junction table (M:M relationship)

This project demonstrates a complete distributed system following enterprise Java patterns and best practices for the final exam requirements.