package service.implementation;

import dao.BookingDAO;
import dao.BookingDAOImpl;
import model.Booking;
import service.BookingService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Date;

public class BookingServiceImpl extends UnicastRemoteObject implements BookingService {
    
    private BookingDAO bookingDAO;
    
    public BookingServiceImpl() throws RemoteException {
        super();
        try {
            this.bookingDAO = new BookingDAOImpl();
            System.out.println("BookingServiceImpl created successfully");
        } catch (Exception e) {
            System.err.println("Error creating BookingServiceImpl: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to initialize BookingService", e);
        }
    }
    
    @Override
    public boolean createBooking(Booking booking) throws RemoteException {
        return bookingDAO.saveBooking(booking);
    }
    
    @Override
    public Booking getBookingById(int bookingId) throws RemoteException {
        return bookingDAO.getBookingById(bookingId);
    }
    
    @Override
    public List<Booking> getAllBookings() throws RemoteException {
        return bookingDAO.getAllBookings();
    }
    
    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) throws RemoteException {
        return bookingDAO.getBookingsByCustomerId(customerId);
    }
    
    @Override
    public boolean updateBooking(Booking booking) throws RemoteException {
        return bookingDAO.updateBooking(booking);
    }
    
    @Override
    public boolean cancelBooking(int bookingId) throws RemoteException {
        return bookingDAO.cancelBooking(bookingId);
    }
    
    @Override
    public boolean reopenBooking(int bookingId, Date newStartDate, Date newEndDate) throws RemoteException {
        return bookingDAO.reopenBooking(bookingId, newStartDate, newEndDate);
    }
    
    @Override
    public List<String[]> getTodaysBookingsWithDetails() throws RemoteException {
        return bookingDAO.getTodaysBookingsWithDetails();
    }
    
    @Override
    public int[] getDashboardStats() throws RemoteException {
        return bookingDAO.getDashboardStats();
    }
    
    @Override
    public List<Object[]> getPendingBookingsReport() throws RemoteException {
        return bookingDAO.getPendingBookingsReport();
    }
    
    @Override
    public boolean approveBooking(int bookingId) throws RemoteException {
        return bookingDAO.approveBooking(bookingId);
    }
    
    @Override
    public boolean rejectBooking(int bookingId) throws RemoteException {
        return bookingDAO.rejectBooking(bookingId);
    }
    
    @Override
    public boolean rejectBookingWithReason(int bookingId, String reason) throws RemoteException {
        return bookingDAO.rejectBookingWithReason(bookingId, reason);
    }
    
    @Override
    public List<Object[]> getActiveRentalsReport() throws RemoteException {
        return bookingDAO.getActiveRentalsReport();
    }
    
    @Override
    public List<Object[]> getMostRentedVehicles() throws RemoteException {
        return bookingDAO.getMostRentedVehicles();
    }
    
    @Override
    public List<Object[]> getVehicleAvailabilityReport(java.util.Date date) throws RemoteException {
        return bookingDAO.getVehicleAvailabilityReport(date);
    }
    
    @Override
    public List<Object[]> getBookingsHistoryReport() throws RemoteException {
        return bookingDAO.getBookingsHistoryReport();
    }
    
    @Override
    public boolean isDuplicateBooking(int customerId, int vehicleId, java.util.Date startDate, java.util.Date endDate) throws RemoteException {
        return bookingDAO.isDuplicateBooking(customerId, vehicleId, startDate, endDate);
    }
    
    @Override
    public boolean isVehicleUnavailable(int vehicleId, java.util.Date startDate, java.util.Date endDate) throws RemoteException {
        return bookingDAO.isVehicleUnavailable(vehicleId, startDate, endDate);
    }
    
    @Override
    public String getNextAvailableDates(int vehicleId, java.util.Date requestedStart, java.util.Date requestedEnd) throws RemoteException {
        return bookingDAO.getNextAvailableDates(vehicleId, requestedStart, requestedEnd);
    }
}