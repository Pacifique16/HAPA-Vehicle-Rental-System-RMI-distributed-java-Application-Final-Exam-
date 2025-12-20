package dao;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class BookingDAOImpl implements BookingDAO {
    
    @Override
    public boolean saveBooking(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id, vehicle_id, start_date, end_date, total_cost, status, booking_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, booking.getCustomerId());
            pst.setInt(2, booking.getVehicleId());
            pst.setDate(3, new java.sql.Date(booking.getStartDate().getTime()));
            pst.setDate(4, new java.sql.Date(booking.getEndDate().getTime()));
            pst.setDouble(5, booking.getTotalCost());
            pst.setString(6, booking.getStatus());
            pst.setTimestamp(7, new Timestamp(booking.getBookingDate().getTime()));
            
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, bookingId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY booking_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, v.model as vehicle_model FROM bookings b " +
                    "JOIN vehicles v ON b.vehicle_id = v.id " +
                    "WHERE b.customer_id = ? ORDER BY b.booking_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.add(booking);
            }
        } catch (Exception e) {
            System.out.println("Database query failed, returning empty list: " + e.getMessage());
            // Return empty list if no bookings table exists yet
        }
        
        return bookings;
    }
    
    @Override
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET start_date = ?, end_date = ?, total_cost = ?, status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setDate(1, new java.sql.Date(booking.getStartDate().getTime()));
            pst.setDate(2, new java.sql.Date(booking.getEndDate().getTime()));
            pst.setDouble(3, booking.getTotalCost());
            pst.setString(4, booking.getStatus());
            pst.setInt(5, booking.getId());
            
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, bookingId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, bookingId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean reopenBooking(int bookingId, Date newStartDate, Date newEndDate) {
        String sql = "UPDATE bookings SET status = 'PENDING', start_date = ?, end_date = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setDate(1, new java.sql.Date(newStartDate.getTime()));
            pst.setDate(2, new java.sql.Date(newEndDate.getTime()));
            pst.setInt(3, bookingId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setVehicleId(rs.getInt("vehicle_id"));
        booking.setStartDate(rs.getDate("start_date"));
        booking.setEndDate(rs.getDate("end_date"));
        booking.setTotalCost(rs.getDouble("total_cost"));
        booking.setStatus(rs.getString("status"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setRejectionReason(rs.getString("rejection_reason"));
        return booking;
    }
}