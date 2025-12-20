package dao;

import model.SimpleBooking;
import java.util.*;

public class SimpleBookingDao {
    private static Map<Integer, SimpleBooking> bookings = new HashMap<>();
    private static int nextId = 1;
    
    public boolean saveBooking(SimpleBooking booking) {
        booking.setId(nextId++);
        bookings.put(booking.getId(), booking);
        return true;
    }
    
    public boolean updateBooking(SimpleBooking booking) {
        if (bookings.containsKey(booking.getId())) {
            bookings.put(booking.getId(), booking);
            return true;
        }
        return false;
    }
    
    public boolean deleteBooking(int bookingId) {
        return bookings.remove(bookingId) != null;
    }
    
    public SimpleBooking getBookingById(int bookingId) {
        return bookings.get(bookingId);
    }
    
    public List<SimpleBooking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }
    
    public List<SimpleBooking> getBookingsByCustomerId(int customerId) {
        return bookings.values().stream()
                .filter(b -> b.getCustomer().getId() == customerId)
                .collect(ArrayList::new, (list, booking) -> list.add(booking), ArrayList::addAll);
    }
    
    public List<SimpleBooking> getBookingsByStatus(String status) {
        return bookings.values().stream()
                .filter(b -> b.getStatus().equals(status))
                .collect(ArrayList::new, (list, booking) -> list.add(booking), ArrayList::addAll);
    }
    
    public boolean isVehicleAvailable(int vehicleId, Date startDate, Date endDate) {
        return bookings.values().stream()
                .noneMatch(b -> b.getVehicle().getId() == vehicleId && 
                          ("APPROVED".equals(b.getStatus()) || "PENDING".equals(b.getStatus())) &&
                          ((startDate.compareTo(b.getEndDate()) <= 0) && (endDate.compareTo(b.getStartDate()) >= 0)));
    }
}