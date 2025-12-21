package controller;

import service.implementation.UserServiceImpl;
import service.implementation.VehicleServiceImpl;
import service.implementation.BookingServiceImpl;
import service.implementation.OTPServiceImpl;
import util.HibernateUtil;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class Server {
    
    private static final int PORT = 3506; // Changed port to avoid conflicts
    
    public static void main(String[] args) {
        try {
            // Initialize Hibernate SessionFactory
            System.out.println("Initializing Hibernate...");
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate initialized successfully!");
            
            // Create RMI registry on specified port
            LocateRegistry.createRegistry(PORT);
            System.out.println("RMI Registry created on port " + PORT);
            
            // Create service implementations
            UserServiceImpl userService = new UserServiceImpl();
            VehicleServiceImpl vehicleService = new VehicleServiceImpl();
            BookingServiceImpl bookingService = new BookingServiceImpl();
            OTPServiceImpl otpService = new OTPServiceImpl();
            
            // Bind services to registry
            Naming.rebind("rmi://localhost:" + PORT + "/UserService", userService);
            Naming.rebind("rmi://localhost:" + PORT + "/VehicleService", vehicleService);
            Naming.rebind("rmi://localhost:" + PORT + "/BookingService", bookingService);
            Naming.rebind("rmi://localhost:" + PORT + "/OTPService", otpService);
            
            System.out.println("HAPA Vehicle Rental Server is running...");
            System.out.println("Services bound to registry:");
            System.out.println("- UserService: rmi://localhost:" + PORT + "/UserService");
            System.out.println("- VehicleService: rmi://localhost:" + PORT + "/VehicleService");
            System.out.println("- BookingService: rmi://localhost:" + PORT + "/BookingService");
            System.out.println("- OTPService: rmi://localhost:" + PORT + "/OTPService");
            System.out.println("Server ready to accept client connections...");
            
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}