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
}