-- HAPA Vehicle Rental System - Sample Data Setup
-- Run this script after starting the server (Hibernate creates tables automatically)
-- This script only inserts sample data for testing
-- Database name: hapa_vehicle_rental_db

-- Insert default admin user with strong password
INSERT INTO users (username, password, email, full_name, phone, role, status) 
VALUES ('admin', 'Admin123!', 'admin@hapa.rw', 'System Administrator', '0788000000', 'admin', 'Active')
ON CONFLICT (username) DO NOTHING;

-- Insert sample customer users with different emails and strong passwords
INSERT INTO users (username, password, email, full_name, phone, role, status) VALUES
('pacifique', 'Pacifique123!', 'harerimanapacifique95@gmail.com', 'Pacifique HARERIMANA', '0789534491', 'customer', 'Active'),
('beatrice', 'Beatrice123!', 'beatrice.nyira@gmail.com', 'Beatrice NYIRAHABIMANA', '0785005381', 'customer', 'Active'),
('christophe', 'Christophe123!', 'chris.nsanzineza@gmail.com', 'Christophe NSANZINEZA', '0788551100', 'customer', 'Active'),
('king', 'King123!', 'king.cyuzuzo@gmail.com', 'King CYUZUZO REGIS', '0789632145', 'customer', 'Active'),
('leandre', 'Leandre123!', 'leandre.ntwari@gmail.com', 'Ntwari Ganza Leandre', '0791889257', 'customer', 'Active'),
('justin', 'Justin123!', 'justin.ntwali@gmail.com', 'Ntwali Justin', '0785263149', 'customer', 'Active'),
('ghislaine', 'Ghislaine123!', 'ghislaine.mugisha@gmail.com', 'Ghislaine MUGISHA', '0786622101', 'customer', 'Active'),
('christian', 'Christian123!', 'christian.hare@gmail.com', 'Christian HARERIMANA', '0787164108', 'customer', 'Active'),
('samuel', 'Samuel123!', 'samuel.kwizera@gmail.com', 'KWIZERA Samuel', '0781111112', 'customer', 'Active'),
('bakundukize', 'Bakundukize123!', 'pacitekno12@gmail.com', 'Pacifique BAKUNDUKIZE', '0785231462', 'customer', 'Active'),
('queen', 'Queen123!', 'queenuwisheja@gmail.com', 'Queen UWISHEJA', '0788888888', 'customer', 'Active'),
('esther', 'Esther123!', 'charmingesther2@gmail.com', 'Esther INGABIRE', '0785236410', 'customer', 'Active')
ON CONFLICT (username) DO NOTHING;

-- Insert sample vehicles (matching your existing data)
INSERT INTO vehicles (model, plate_number, category, seats, fuel_type, transmission, price_per_day, status, image_path) VALUES
('Toyota Hiace 2022', 'RAD 450 D', 'Van', 12, 'Diesel', 'Manual', 150000, 'Available', 'images/hiace.png'),
('Bentley Continental GT', 'RAD 600 B', 'SUV', 2, 'Petrol', 'Automatic', 600000, 'Available', 'images/bentley.png'),
('Toyota Hiace 2010', 'RAD 963 E', 'Van', 12, 'Petrol', 'Manual', 120000, 'Available', 'images/van.png'),
('Toyota Prius 2014', 'RAJ 852 B', 'SUV', 5, 'Hybrid', 'Automatic', 45000, 'Available', 'images/prius1.png'),
('Ford Ranger 2022', 'RAE 110 E', 'Pickup', 5, 'Petrol', 'Automatic', 100000, 'Available', 'images/ranger.png'),
('Limousine Stretch 2023', 'RAG 900 L', 'Coupe', 8, 'Petrol', 'Automatic', 500000, 'Available', 'images/limousine.png'),
('Hummer H2 2022', 'RAB 777 A', 'SUV', 6, 'Petrol', 'Automatic', 320000, 'Available', 'images/hummer.jpg'),
('Kia Sorento 2011', 'RAC 456 H', 'Hatchback', 7, 'Diesel', 'Automatic', 50000, 'Available', 'images/kiaa.png'),
('KIA SORENTO 2011', 'RAB 820 A', 'SUV', 5, 'Petrol', 'Automatic', 50000, 'Available', 'images/sorento.png'),
('McLaren', 'RAD 801 L', 'Luxury', 2, 'Electric', 'Automatic', 200000, 'Available', 'images/mclaren.png'),
('Lamborghini Huracan', 'RAD 800 L', 'SUV', 2, 'Petrol', 'Automatic', 800000, 'Available', 'images/lamborghini.png'),
('Nissan', 'RAH 789 V', 'SUV', 4, 'Petrol', 'Manual', 150000, 'Available', 'images/nissan.png'),
('Kia Sorento 2011', 'RAC 456 K', 'Hatchback', 7, 'Diesel', 'Automatic', 50000, 'Available', 'images/kiaa.png'),
('Hyundai Strarex 2022', 'RAH 897 C', 'Van', 9, 'Petrol', 'Automatic', 170000, 'Available', 'images/strarex.png'),
('Toyota Coaster 2018', 'RAI 110 B', 'Bus', 16, 'Diesel', 'Manual', 225000, 'Available', 'images/coaster.png'),
('Rolls Royce Phantom', 'RAD 900 R', 'SUV', 4, 'Petrol', 'Automatic', 900000, 'Available', 'images/royce.png'),
('Land Cruiser TXL 2023', 'RAA 016 A', 'SUV', 7, 'Petrol', 'Manual', 130000, 'Available', 'images/cruiser.png'),
('Mercedes G-Wagon G63', 'RAD 500 G', 'SUV', 6, 'Petrol', 'Automatic', 500000, 'Available', 'images/g class.jpg'),
('Toyota Corolla 2014', 'RAF 990 F', 'SUV', 5, 'Electric', 'Automatic', 45000, 'Available', 'images/prius.png'),
('Range Rover Sport 2010', 'RAI 789 H', 'SUV', 5, 'Petrol', 'Automatic', 350000, 'Available', 'images/rover.png'),
('RAV 4 2002', 'RAC 300 C', 'SUV', 5, 'Diesel', 'Manual', 45000, 'Available', 'images/rav4.png'),
('Toyota Hiace 2017', 'RAI 888 H', 'Van', 15, 'Petrol', 'Manual', 150000, 'Available', 'images/hiace1.png'),
('Hyundai Tucson 2012', 'RAC 123 M', 'Hatchback', 5, 'Diesel', 'Automatic', 50000, 'Available', 'images/tucson.png'),
('Toyota Prius 2014', 'RAD 493 A', 'Coupe', 5, 'Hybrid', 'Automatic', 45000, 'Available', 'images/prius2.png'),
('Kia Sorento 2012', 'RAH 741 J', 'SUV', 7, 'Diesel', 'Automatic', 45000, 'Available', 'images/sorento.png'),
('Audi A6 2023', 'RAB 505 H', 'Sedan', 4, 'Electric', 'Automatic', 220000, 'Available', 'images/audi.png'),
('Kia Sportage 2012', 'RAH 789 B', 'Hatchback', 5, 'Diesel', 'Automatic', 50000, 'Available', 'images/sportage.png'),
('Prado TXL 2023', 'RAJ 709 H', 'SUV', 7, 'Diesel', 'Manual', 150000, 'Available', 'images/prado.png')
ON CONFLICT (plate_number) DO NOTHING;

-- Insert sample bookings (recent dates for testing)
INSERT INTO bookings (user_id, vehicle_id, start_date, end_date, total_cost, status, booking_date, rejection_reason) VALUES
(1, 1, '2024-12-20', '2024-12-22', 300000, 'APPROVED', '2024-12-18 10:30:00', NULL),
(2, 3, '2024-12-21', '2024-12-25', 480000, 'APPROVED', '2024-12-19 14:20:00', NULL),
(3, 5, '2024-12-22', '2024-12-26', 400000, 'PENDING', '2024-12-20 09:15:00', NULL),
(4, 2, '2024-12-15', '2024-12-18', 1800000, 'REJECTED', '2024-12-13 16:45:00', 'Incomplete documentation provided'),
(5, 7, '2024-12-25', '2024-12-30', 1600000, 'CANCELLED', '2024-12-22 11:30:00', NULL)
ON CONFLICT DO NOTHING;

SELECT 'Sample data inserted successfully!' as message;