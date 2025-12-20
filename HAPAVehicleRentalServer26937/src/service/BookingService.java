package service;

import model.Booking;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Date;

public interface BookingService extends Remote {
    boolean createBooking(Booking booking) throws RemoteException;
    Booking getBookingById(int bookingId) throws RemoteException;
    List<Booking> getAllBookings() throws RemoteException;
    List<Booking> getBookingsByCustomerId(int customerId) throws RemoteException;
    boolean updateBooking(Booking booking) throws RemoteException;
    boolean cancelBooking(int bookingId) throws RemoteException;
    boolean reopenBooking(int bookingId, Date newStartDate, Date newEndDate) throws RemoteException;
}