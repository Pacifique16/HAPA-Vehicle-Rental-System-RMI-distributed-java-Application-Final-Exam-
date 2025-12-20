-- HAPA Vehicle Rental System Database Setup
-- Create database
CREATE DATABASE hapa_vehicle_rental_db;

-- Use the database
\c hapa_vehicle_rental_db;

-- Create tables (Hibernate will auto-create these, but here's the structure for reference)

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'customer',
    status VARCHAR(20) DEFAULT 'Active'
);

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

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id SERIAL PRIMARY KEY,
    plate_number VARCHAR(20) UNIQUE NOT NULL,
    model VARCHAR(50) NOT NULL,
    category VARCHAR(30) NOT NULL,
    fuel_type VARCHAR(20),
    transmission VARCHAR(20),
    seats INTEGER,
    price_per_day DECIMAL(10,2) NOT NULL,
    image_path VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Available'
);

-- Bookings table (Many-to-One with users and vehicles)
CREATE TABLE IF NOT EXISTS bookings (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    vehicle_id INTEGER REFERENCES vehicles(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    rejection_reason TEXT,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

-- Insert sample data
INSERT INTO users (username, password, full_name, phone, email, role) VALUES
('admin', 'admin123', 'System Administrator', '+250788123456', 'admin@hapa.com', 'admin'),
('john_doe', 'password123', 'John Doe', '+250788654321', 'john@email.com', 'customer'),
('jane_smith', 'password123', 'Jane Smith', '+250788987654', 'jane@email.com', 'customer');

INSERT INTO vehicles (plate_number, model, category, fuel_type, transmission, seats, price_per_day, status) VALUES
('RAD 123A', 'Toyota Prius', 'Sedan', 'Hybrid', 'Automatic', 5, 50.00, 'Available'),
('RAD 456B', 'Toyota RAV4', 'SUV', 'Petrol', 'Automatic', 7, 80.00, 'Available'),
('RAD 789C', 'Mercedes G-Class', 'Luxury SUV', 'Petrol', 'Automatic', 5, 150.00, 'Available'),
('RAD 321D', 'Toyota Hiace', 'Van', 'Diesel', 'Manual', 14, 100.00, 'Available');

-- Create indexes for better performance
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_vehicle ON bookings(vehicle_id);
CREATE INDEX idx_bookings_dates ON bookings(start_date, end_date);
CREATE INDEX idx_vehicles_status ON vehicles(status);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);