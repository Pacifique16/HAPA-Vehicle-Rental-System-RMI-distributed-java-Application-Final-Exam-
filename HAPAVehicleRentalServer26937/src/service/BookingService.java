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
    List<String[]> getTodaysBookingsWithDetails() throws RemoteException;
    int[] getDashboardStats() throws RemoteException;
    List<Object[]> getPendingBookingsReport() throws RemoteException;
    boolean approveBooking(int bookingId) throws RemoteException;
    boolean rejectBooking(int bookingId) throws RemoteException;
    boolean rejectBookingWithReason(int bookingId, String reason) throws RemoteException;
    List<Object[]> getActiveRentalsReport() throws RemoteException;
    List<Object[]> getMostRentedVehicles() throws RemoteException;
    List<Object[]> getVehicleAvailabilityReport(java.util.Date date) throws RemoteException;
    List<Object[]> getBookingsHistoryReport() throws RemoteException;
    boolean isDuplicateBooking(int customerId, int vehicleId, java.util.Date startDate, java.util.Date endDate) throws RemoteException;
    boolean isVehicleUnavailable(int vehicleId, java.util.Date startDate, java.util.Date endDate) throws RemoteException;
    String getNextAvailableDates(int vehicleId, java.util.Date requestedStart, java.util.Date requestedEnd) throws RemoteException;
}