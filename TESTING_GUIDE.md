# Testing Guide

## Prerequisites

1. **PostgreSQL** installed and running
2. **Java 8+** installed
3. **Required JAR files** in lib folders

## Setup Steps

### 1. Database Setup
```sql
-- Run in PostgreSQL
CREATE DATABASE hapa_vehicle_rental;
\c hapa_vehicle_rental;
\i create_database.sql
\i database_setup.sql
```

### 2. Server Dependencies
Add to `HAPAVehicleRentalServer26937/lib/`:
- hibernate-core-5.6.15.Final.jar
- postgresql-42.7.7.jar
- activemq-broker-5.18.3.jar
- activemq-client-5.18.3.jar
- geronimo-jms_1.1_spec-1.1.1.jar
- slf4j-api-1.7.36.jar

### 3. Client Dependencies
Add to `HapaVehicleRentalClient26937/lib/`:
- itextpdf-5.5.13.1.jar

## Testing Workflow

### 1. Start Server
```bash
cd HAPAVehicleRentalServer26937/src
java controller.Server
```
Expected output:
```
✅ Embedded ActiveMQ Broker started
✅ HAPA Vehicle Rental Server is running on port 3506
```

### 2. Start Client
```bash
cd HapaVehicleRentalClient26937/src
java view.LoginForm
```

### 3. Test Features

#### Admin Login
- Username: `admin`
- Password: `admin123`
- Test: Dashboard, user management, vehicle management, reports

#### Customer Registration
- Register new customer account
- Verify OTP email delivery
- Test: Vehicle booking, profile management

#### Core Functionality
- **User Management**: Add/edit/delete users
- **Vehicle Management**: Add/edit/delete vehicles
- **Booking System**: Create/approve/reject bookings
- **Reports**: Export PDF/CSV reports
- **Session Management**: Test timeout and logout

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running on port 5432
- Check database credentials in hibernate.cfg.xml

### RMI Connection Issues
- Ensure server is running on port 3506
- Check firewall settings

### Email/OTP Issues
- Verify Gmail SMTP configuration
- Check ActiveMQ broker startup

### Missing Dependencies
- Ensure all JAR files are in correct lib folders
- Check classpath configuration in IDE