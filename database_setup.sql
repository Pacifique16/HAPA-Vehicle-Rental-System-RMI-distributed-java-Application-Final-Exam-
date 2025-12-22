-- HAPA Vehicle Rental System Database Setup
-- Run this script in PostgreSQL to create the complete database

-- Create database (run this first)
-- CREATE DATABASE hapa_vehicle_rental;
-- \c hapa_vehicle_rental;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS maintenance CASCADE;
DROP TABLE IF EXISTS vehicles CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'customer',
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Categories table
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
);

-- Create Vehicles table
CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    plate_number VARCHAR(20) UNIQUE NOT NULL,
    category_id INTEGER REFERENCES categories(id),
    seats INTEGER NOT NULL,
    fuel_type VARCHAR(20) NOT NULL,
    transmission VARCHAR(20) NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Available',
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Bookings table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    vehicle_id INTEGER REFERENCES vehicles(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by INTEGER REFERENCES users(id),
    approval_date TIMESTAMP
);

-- Create Maintenance table
CREATE TABLE maintenance (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER REFERENCES vehicles(id),
    maintenance_type VARCHAR(100) NOT NULL,
    description TEXT,
    cost DECIMAL(10,2),
    maintenance_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user
INSERT INTO users (username, password, email, full_name, phone, role, status) 
VALUES ('admin', 'admin123', 'admin@hapa.com', 'System Administrator', '0788123456', 'admin', 'Active');

-- Insert vehicle categories
INSERT INTO categories (name, description) VALUES 
('Sedan', 'Comfortable passenger cars'),
('SUV', 'Sport Utility Vehicles'),
('Hatchback', 'Compact cars with rear door'),
('Pickup', 'Light trucks for cargo'),
('Van', 'Large passenger or cargo vehicles'),
('Luxury', 'Premium and luxury vehicles');

-- Insert sample vehicles
INSERT INTO vehicles (model, plate_number, category_id, seats, fuel_type, transmission, price_per_day, status, image_path) VALUES
('Toyota Prius', 'RAD001A', 1, 5, 'Hybrid', 'Automatic', 25000, 'Available', 'prius.png'),
('Toyota RAV4', 'RAD002B', 2, 5, 'Petrol', 'Automatic', 35000, 'Available', 'rav4.png'),
('Kia Sportage', 'RAD003C', 2, 5, 'Petrol', 'Automatic', 40000, 'Available', 'sportage.png'),
('Toyota Hiace', 'RAD004D', 5, 14, 'Diesel', 'Manual', 50000, 'Available', 'hiace.png'),
('Ford Ranger', 'RAD005E', 4, 5, 'Diesel', 'Manual', 45000, 'Available', 'ranger.png'),
('Audi A4', 'RAD006F', 6, 5, 'Petrol', 'Automatic', 60000, 'Available', 'audi.png'),
('BMW X5', 'RAD007G', 6, 7, 'Petrol', 'Automatic', 80000, 'Available', 'bmw.png'),
('Mercedes G-Class', 'RAD008H', 6, 5, 'Petrol', 'Automatic', 120000, 'Available', 'g class.jpg'),
('Lamborghini Huracan', 'RAD009I', 6, 2, 'Petrol', 'Automatic', 200000, 'Available', 'lamborghini.png'),
('Rolls Royce Phantom', 'RAD010J', 6, 5, 'Petrol', 'Automatic', 300000, 'Available', 'royce.png'),
('Toyota Prado', 'RAD011K', 2, 7, 'Diesel', 'Automatic', 55000, 'Available', 'prado.png'),
('Hyundai Tucson', 'RAD012L', 2, 5, 'Petrol', 'Automatic', 38000, 'Available', 'tucson.png'),
('Kia Sorento', 'RAD013M', 2, 7, 'Petrol', 'Automatic', 42000, 'Available', 'sorento.png'),
('Nissan Patrol', 'RAD014N', 2, 8, 'Petrol', 'Automatic', 65000, 'Available', 'nissan.png'),
('Toyota Coaster', 'RAD015O', 5, 30, 'Diesel', 'Manual', 80000, 'Available', 'coaster.png'),
('Hyundai Strarex', 'RAD016P', 5, 12, 'Diesel', 'Manual', 45000, 'Available', 'strarex.png'),
('McLaren 720S', 'RAD017Q', 6, 2, 'Petrol', 'Automatic', 250000, 'Available', 'mclaren.png'),
('Bentley Continental', 'RAD018R', 6, 4, 'Petrol', 'Automatic', 180000, 'Available', 'bentley.png'),
('Hummer H2', 'RAD019S', 6, 8, 'Petrol', 'Automatic', 100000, 'Available', 'hummer.jpg'),
('Land Rover Discovery', 'RAD020T', 2, 7, 'Diesel', 'Automatic', 70000, 'Available', 'rover.png'),
('Toyota Prius V', 'RAD021U', 1, 7, 'Hybrid', 'Automatic', 30000, 'Available', 'prius1.png'),
('Toyota Prius C', 'RAD022V', 1, 5, 'Hybrid', 'Automatic', 28000, 'Available', 'prius2.png'),
('Kia Cerato', 'RAD023W', 1, 5, 'Petrol', 'Manual', 22000, 'Available', 'kiaa.png'),
('Toyota Hiace Super GL', 'RAD024X', 5, 10, 'Diesel', 'Manual', 48000, 'Available', 'hiace1.png'),
('Limousine', 'RAD025Y', 6, 8, 'Petrol', 'Automatic', 150000, 'Available', 'limousine.png'),
('Toyota Land Cruiser', 'RAD026Z', 2, 8, 'Diesel', 'Automatic', 75000, 'Available', 'cruiser.png');

-- Insert sample bookings
INSERT INTO bookings (user_id, vehicle_id, start_date, end_date, total_cost, status, booking_date) VALUES
(1, 1, '2024-01-15', '2024-01-18', 75000, 'COMPLETED', '2024-01-10 10:30:00'),
(1, 3, '2024-01-20', '2024-01-25', 200000, 'APPROVED', '2024-01-18 14:20:00'),
(1, 5, '2024-02-01', '2024-02-05', 180000, 'PENDING', '2024-01-28 09:15:00');

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_vehicles_status ON vehicles(status);
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_vehicle_id ON bookings(vehicle_id);
CREATE INDEX idx_bookings_status ON bookings(status);

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

-- Display setup completion message
SELECT 'Database setup completed successfully!' as message;