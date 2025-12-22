# HAPA Vehicle Rental System

A distributed Java RMI application for vehicle rental management with admin and customer interfaces.

## Features

- **User Management**: Registration, authentication, profile management
- **Vehicle Management**: Add, update, delete vehicles with availability tracking
- **Booking System**: Vehicle reservations with approval workflow
- **OTP Authentication**: Email-based verification with ActiveMQ messaging
- **Session Management**: Secure user sessions with timeout handling
- **Reports & Export**: PDF and CSV export functionality
- **Real-time Dashboard**: Statistics and booking overview

## Architecture

- **Server**: RMI services, Hibernate ORM, PostgreSQL database, ActiveMQ messaging
- **Client**: Swing GUI with admin and customer dashboards
- **Communication**: Java RMI for distributed services

## Technology Stack

- Java 8+
- Hibernate 5.6.15
- PostgreSQL 42.7.7
- ActiveMQ 5.18.3
- iText PDF 5.5.13
- Java Swing GUI

## Quick Start

1. Setup PostgreSQL database (see DATABASE_SETUP.md)
2. Run HAPAVehicleRentalServer26937/src/controller/Server.java
3. Run HapaVehicleRentalClient26937/src/view/LoginForm.java

## Default Login

**Admin**: username: `admin`, password: `admin123`
**Customer**: Register new account or use existing credentials

## Project Structure

```
├── HAPAVehicleRentalServer26937/    # Server-side RMI services
├── HapaVehicleRentalClient26937/    # Client-side GUI application
├── create_database.sql              # Database creation script
├── database_setup.sql               # Sample data insertion
└── database_migration.sql           # Database updates
```