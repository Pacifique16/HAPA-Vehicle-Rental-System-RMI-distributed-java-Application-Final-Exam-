package controller;

import service.implementation.UserServiceImpl;
import service.implementation.VehicleServiceImpl;
import service.implementation.BookingServiceImpl;
import service.implementation.OTPServiceImpl;
import service.implementation.SessionServiceImpl;
import util.HibernateUtil;
import util.SimpleBrokerStarter;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class Server {
    
    private static final int PORT = 3506; // Changed port to avoid conflicts
    
    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();
            
            SimpleBrokerStarter.startBroker();
            
            LocateRegistry.createRegistry(PORT);
            
            UserServiceImpl userService = new UserServiceImpl();
            VehicleServiceImpl vehicleService = new VehicleServiceImpl();
            BookingServiceImpl bookingService = new BookingServiceImpl();
            OTPServiceImpl otpService = new OTPServiceImpl();
            SessionServiceImpl sessionService = new SessionServiceImpl();
            
            Naming.rebind("rmi://localhost:" + PORT + "/UserService", userService);
            Naming.rebind("rmi://localhost:" + PORT + "/VehicleService", vehicleService);
            Naming.rebind("rmi://localhost:" + PORT + "/BookingService", bookingService);
            Naming.rebind("rmi://localhost:" + PORT + "/OTPService", otpService);
            Naming.rebind("rmi://localhost:" + PORT + "/SessionService", sessionService);
            
            System.out.println("HAPA Vehicle Rental Server is running on port " + PORT);
            
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}