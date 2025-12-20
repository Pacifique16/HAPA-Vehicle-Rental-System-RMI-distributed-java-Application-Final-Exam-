package service.implementation;

import model.Booking;
import service.BookingService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class SimpleBookingService extends UnicastRemoteObject implements BookingService {
    
    public SimpleBookingService() throws RemoteException {
        super();
    }
    
    @Override
    public boolean createBooking(Booking booking) throws RemoteException {
        return true;
    }
    
    @Override
    public boolean updateBooking(Booking booking) throws RemoteException {
        return true;
    }
    
    @Override
    public boolean deleteBooking(int bookingId) throws RemoteException {
        return true;
    }
    
    @Override
    public Booking getBookingById(int bookingId) throws RemoteException {
        return new Booking();
    }
    
    @Override
    public List<Booking> getAllBookings() throws RemoteException {
        return new ArrayList<>();
    }
    
    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) throws RemoteException {
        return new ArrayList<>();
    }
    
    @Override
    public List<Booking> getPendingBookings() throws RemoteException {
        return new ArrayList<>();
    }
    
    @Override
    public boolean approveBooking(int bookingId) throws RemoteException {
        return true;
    }
    
    @Override
    public boolean rejectBooking(int bookingId, String reason) throws RemoteException {
        return true;
    }
    
    @Override
    public boolean cancelBooking(int bookingId) throws RemoteException {
        return true;
    }
    
    @Override
    public boolean isVehicleAvailable(int vehicleId, Date startDate, Date endDate) throws RemoteException {
        return true;
    }
    
    @Override
    public double calculateTotalCost(int vehicleId, Date startDate, Date endDate) throws RemoteException {
        return 100.0;
    }
}