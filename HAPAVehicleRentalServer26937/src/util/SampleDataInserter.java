package util;

import model.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SampleDataInserter {
    
    public static void main(String[] args) {
        insertSampleVehicles();
        System.out.println("Sample data inserted successfully!");
        System.exit(0);
    }
    
    private static void insertSampleVehicles() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Sample vehicles data
            String[][] vehicleData = {
                {"RAD 800 L", "Lamborghini Huracan", "SUV", "800000", "images/lamborghini.png", "Petrol", "Automatic", "2", "Available"},
                {"RAD 450 D", "Toyota Hiace 2022", "Van", "150000", "images/hiace.png", "Diesel", "Manual", "12", "Available"},
                {"RAD 600 B", "Bentley Contintental GT", "SUV", "600000", "images/bentley.png", "Petrol", "Automatic", "2", "Available"},
                {"RAH 789 V", "Nissan", "SUV", "150000", "images/nissan.png", "Petrol", "Manual", "4", "Available"},
                {"RAC 456 K", "Kia Sorento 2011", "Hatchback", "50000", "images/kiaa.png", "Diesel", "Automatic", "7", "Available"},
                {"RAD 963 E", "Toyota Hiace 2010", "Van", "120000", "images/van.png", "Petrol", "Manual", "12", "Available"},
                {"RAJ 852 B", "Toyota Prius 2014", "SUV", "45000", "images/prius1.png", "Hybrid", "Automatic", "5", "Available"},
                {"RAJ 709 H", "Prado TXL 2023", "SUV", "150000", "images/prado.png", "Diesel", "Manual", "7", "Available"},
                {"RAE 110 E", "Ford Ranger 2022", "Pickup", "100000", "images/ranger.png", "Petrol", "Automatic", "5", "Available"},
                {"RAG 900 L", "Limousine Stretch 2023", "Coupe", "500000", "images/limousine.png", "Petrol", "Automatic", "8", "Available"},
                {"RAB 820 A", "KIA SORENTO 2011", "SUV", "50000", "images/sorento.png", "Petrol", "Automatic", "5", "Available"},
                {"RAB 777 A", "Hummer H2 2022", "SUV", "320000", "images/hummer.jpg", "Petrol", "Automatic", "6", "Available"}
            };
            
            for (String[] data : vehicleData) {
                Vehicle vehicle = new Vehicle();
                vehicle.setPlateNumber(data[0]);
                vehicle.setModel(data[1]);
                vehicle.setCategory(data[2]);
                vehicle.setPricePerDay(Double.parseDouble(data[3]));
                vehicle.setImagePath(data[4]);
                vehicle.setFuelType(data[5]);
                vehicle.setTransmission(data[6]);
                vehicle.setSeats(Integer.parseInt(data[7]));
                vehicle.setStatus(data[8]);
                
                session.save(vehicle);
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}