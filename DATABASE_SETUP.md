# Database Setup Guide

## PostgreSQL Installation

### Windows
1. Download PostgreSQL from https://www.postgresql.org/download/windows/
2. Install with default settings
3. Remember the password for `postgres` user

### Configuration
- **Host**: localhost
- **Port**: 5432
- **Database**: hapa_vehicle_rental
- **Username**: postgres
- **Password**: [your_password]

## Database Creation

### 1. Connect to PostgreSQL
```bash
psql -U postgres -h localhost
```

### 2. Create Database
```sql
CREATE DATABASE hapa_vehicle_rental;
\c hapa_vehicle_rental;
```

### 3. Run Setup Scripts
```sql
-- Create tables and relationships
\i create_database.sql

-- Insert sample data
\i database_setup.sql

-- Apply any updates (optional)
\i database_migration.sql
```

## Database Schema

### Core Tables
- **users**: User accounts (admin/customer)
- **vehicles**: Vehicle inventory
- **bookings**: Rental bookings
- **categories**: Vehicle categories
- **maintenance**: Vehicle maintenance records

### Sample Data Included
- 1 Admin user (admin/admin123)
- 25+ Sample vehicles
- Vehicle categories
- Sample bookings

## Connection Configuration

Update `HAPAVehicleRentalServer26937/src/hibernate.cfg.xml`:
```xml
<property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/hapa_vehicle_rental</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">your_password</property>
```

## Verification

### Check Tables
```sql
\dt
-- Should show: bookings, categories, maintenance, users, vehicles
```

### Check Sample Data
```sql
SELECT COUNT(*) FROM users;    -- Should return 1+
SELECT COUNT(*) FROM vehicles; -- Should return 25+
SELECT COUNT(*) FROM bookings; -- Should return sample bookings
```

## Troubleshooting

### Connection Issues
- Verify PostgreSQL service is running
- Check port 5432 is not blocked
- Confirm database name and credentials

### Permission Issues
- Ensure postgres user has full privileges
- Grant permissions if needed:
```sql
GRANT ALL PRIVILEGES ON DATABASE hapa_vehicle_rental TO postgres;
```