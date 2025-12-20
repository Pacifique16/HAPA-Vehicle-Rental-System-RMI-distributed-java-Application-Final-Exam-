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
}