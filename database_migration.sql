-- Migration script to add new tables to existing hapa_vehicle_rental_system_db
-- Run this on your existing database

-- Connect to your existing database
\c hapa_vehicle_rental_system_db;

-- Add new columns to existing users table if they don't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Active';

-- Add new columns to existing vehicles table if they don't exist  
ALTER TABLE vehicles ADD COLUMN IF NOT EXISTS fuel_type VARCHAR(20);
ALTER TABLE vehicles ADD COLUMN IF NOT EXISTS transmission VARCHAR(20);
ALTER TABLE vehicles ADD COLUMN IF NOT EXISTS seats INTEGER;
ALTER TABLE vehicles ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Available';

-- Add new columns to existing bookings table if they don't exist
ALTER TABLE bookings ADD COLUMN IF NOT EXISTS rejection_reason TEXT;
ALTER TABLE bookings ADD COLUMN IF NOT EXISTS booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Create new tables for additional entities

-- User profiles table (One-to-One with users)
CREATE TABLE IF NOT EXISTS user_profiles (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    date_of_birth DATE,
    address TEXT,
    national_id VARCHAR(50),
    driving_license VARCHAR(50),
    emergency_contact VARCHAR(20),
    profile_picture VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Maintenance table
CREATE TABLE IF NOT EXISTS maintenance (
    id SERIAL PRIMARY KEY,
    maintenance_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    maintenance_date DATE NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    performed_by VARCHAR(100),
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    next_maintenance_date DATE
);

-- Vehicle_Maintenance junction table (Many-to-Many)
CREATE TABLE IF NOT EXISTS vehicle_maintenance (
    maintenance_id INTEGER REFERENCES maintenance(id) ON DELETE CASCADE,
    vehicle_id INTEGER REFERENCES vehicles(id) ON DELETE CASCADE,
    PRIMARY KEY (maintenance_id, vehicle_id)
);

-- Add sample admin user if it doesn't exist
INSERT INTO users (username, password, full_name, phone, email, role, status) 
SELECT 'admin', 'admin123', 'System Administrator', '+250788123456', 'admin@hapa.com', 'admin', 'Active'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Update existing data to have proper status
UPDATE users SET status = 'Active' WHERE status IS NULL;
UPDATE vehicles SET status = 'Available' WHERE status IS NULL;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_bookings_customer ON bookings(customer_id);
CREATE INDEX IF NOT EXISTS idx_bookings_vehicle ON bookings(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_bookings_dates ON bookings(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_vehicles_status ON vehicles(status);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);