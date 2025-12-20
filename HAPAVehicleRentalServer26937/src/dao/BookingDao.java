package dao;

import model.Booking;
import java.util.List;
import java.util.Date;

public interface BookingDAO {
    boolean saveBooking(Booking booking);
    Booking getBookingById(int bookingId);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByCustomerId(int customerId);
    boolean updateBooking(Booking booking);
    boolean deleteBooking(int bookingId);
    boolean cancelBooking(int bookingId);
    boolean reopenBooking(int bookingId, Date newStartDate, Date newEndDate);
    List<String[]> getTodaysBookingsWithDetails();
    int[] getDashboardStats();
    List<Object[]> getPendingBookingsReport();
    boolean approveBooking(int bookingId);
    boolean rejectBooking(int bookingId);
    boolean rejectBookingWithReason(int bookingId, String reason);
    List<Object[]> getActiveRentalsReport();
    List<Object[]> getMostRentedVehicles();
    List<Object[]> getVehicleAvailabilityReport(java.util.Date date);
    List<Object[]> getBookingsHistoryReport();
    boolean isDuplicateBooking(int customerId, int vehicleId, java.util.Date startDate, java.util.Date endDate);
    boolean isVehicleUnavailable(int vehicleId, java.util.Date startDate, java.util.Date endDate);
    String getNextAvailableDates(int vehicleId, java.util.Date requestedStart, java.util.Date requestedEnd);
}