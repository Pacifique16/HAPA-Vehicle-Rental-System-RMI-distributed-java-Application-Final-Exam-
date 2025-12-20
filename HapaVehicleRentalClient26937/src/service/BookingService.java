package service;

import model.Booking;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Date;

public interface BookingService extends Remote {
    boolean addBooking(Booking booking) throws RemoteException;
    boolean createBooking(Booking booking) throws RemoteException;
    boolean updateBooking(Booking booking) throws RemoteException;
    boolean deleteBooking(int id) throws RemoteException;
    Booking findById(int id) throws RemoteException;
    List<Booking> getAllBookings() throws RemoteException;
    List<Booking> getBookingsByCustomerId(int customerId) throws RemoteException;
    List<Booking> getPendingBookings() throws RemoteException;
    boolean approveBooking(int bookingId) throws RemoteException;
    boolean rejectBooking(int bookingId) throws RemoteException;
    int countBookings() throws RemoteException;
    boolean cancelBooking(int bookingId) throws RemoteException;
    boolean reopenBooking(int bookingId, Date newStartDate, Date newEndDate) throws RemoteException;
}