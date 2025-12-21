package dao;

import model.Booking;
import java.util.List;
import java.util.Date;

public interface BookingDAO {
    boolean saveBooking(Booking booking);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByCustomerId(int customerId);
    Booking getBookingById(int bookingId);
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
    List<Object[]> getVehicleAvailabilityReport(Date date);
    List<Object[]> getBookingsHistoryReport();
    boolean isDuplicateBooking(int customerId, int vehicleId, Date startDate, Date endDate);
    boolean isVehicleUnavailable(int vehicleId, Date startDate, Date endDate);
    String getNextAvailableDates(int vehicleId, Date requestedStart, Date requestedEnd);
}