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
    
    @Override
    public List<String[]> getTodaysBookingsWithDetails() {
        List<String[]> bookings = new ArrayList<>();
        String sql = "SELECT u.full_name, v.model, b.start_date, b.end_date, b.status, b.total_cost " +
                    "FROM bookings b " +
                    "JOIN users u ON b.customer_id = u.id " +
                    "JOIN vehicles v ON b.vehicle_id = v.id " +
                    "WHERE DATE(b.start_date) = CURRENT_DATE " +
                    "ORDER BY b.id DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                String[] row = new String[6];
                row[0] = rs.getString("full_name");
                row[1] = rs.getString("model");
                row[2] = rs.getDate("start_date").toString();
                row[3] = rs.getDate("end_date").toString();
                row[4] = rs.getString("status");
                row[5] = String.format("$%.2f", rs.getDouble("total_cost"));
                bookings.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bookings;
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
    
    @Override
    public int[] getDashboardStats() {
        int[] stats = new int[4];
        String sql = "SELECT " +
                    "(SELECT COUNT(*) FROM vehicles) as total_vehicles, " +
                    "(SELECT COUNT(*) FROM users) as total_users, " +
                    "(SELECT COUNT(*) FROM bookings WHERE status != 'REJECTED') as total_rentals, " +
                    "(SELECT COUNT(*) FROM vehicles v WHERE v.status != 'Maintenance' " +
                    " AND NOT EXISTS (SELECT 1 FROM bookings b WHERE b.vehicle_id = v.id " +
                    " AND b.status NOT IN ('CANCELLED', 'REJECTED') " +
                    " AND CURRENT_DATE BETWEEN b.start_date AND b.end_date)) as available_today";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (rs.next()) {
                stats[0] = rs.getInt("total_vehicles");
                stats[1] = rs.getInt("total_users");
                stats[2] = rs.getInt("total_rentals");
                stats[3] = rs.getInt("available_today");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return stats;
    }
    
    @Override
    public List<Object[]> getPendingBookingsReport() {
        List<Object[]> bookings = new ArrayList<>();
        String sql = "SELECT b.id, u.full_name, u.phone, v.model, v.plate_number, " +
                    "b.start_date, b.end_date, b.total_cost, b.status " +
                    "FROM bookings b " +
                    "JOIN users u ON b.customer_id = u.id " +
                    "JOIN vehicles v ON b.vehicle_id = v.id " +
                    "WHERE b.status = 'PENDING' " +
                    "ORDER BY b.start_date ASC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = new Object[9];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("full_name");
                row[2] = rs.getString("phone");
                row[3] = rs.getString("model");
                row[4] = rs.getString("plate_number");
                row[5] = rs.getDate("start_date").toString();
                row[6] = rs.getDate("end_date").toString();
                row[7] = String.format("%.0f RWF", rs.getDouble("total_cost"));
                row[8] = rs.getString("status");
                bookings.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    @Override
    public boolean approveBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'APPROVED' WHERE id = ?";
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
    public boolean rejectBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'REJECTED' WHERE id = ?";
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
    public boolean rejectBookingWithReason(int bookingId, String reason) {
        String sql = "UPDATE bookings SET status = 'REJECTED', rejection_reason = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, reason);
            pst.setInt(2, bookingId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Object[]> getActiveRentalsReport() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT u.full_name, v.model, b.start_date, b.end_date, b.total_cost, b.status " +
                     "FROM bookings b " +
                     "JOIN vehicles v ON b.vehicle_id = v.id " +
                     "JOIN users u ON b.customer_id = u.id " +
                     "WHERE b.status = 'APPROVED' AND b.start_date <= CURRENT_DATE AND b.end_date >= CURRENT_DATE " +
                     "ORDER BY b.start_date ASC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Object[] {
                    rs.getString("full_name"),
                    rs.getString("model"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    String.format("%,.0f RWF", rs.getDouble("total_cost")),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<Object[]> getMostRentedVehicles() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT v.model, " +
                     "COUNT(CASE WHEN b.status IN ('APPROVED', 'EXPIRED') THEN 1 END) AS times_rented, " +
                     "COALESCE(SUM(CASE WHEN b.status IN ('APPROVED', 'EXPIRED') THEN b.total_cost END), 0) AS total_income " +
                     "FROM vehicles v " +
                     "LEFT JOIN bookings b ON v.id = b.vehicle_id " +
                     "GROUP BY v.model " +
                     "ORDER BY times_rented DESC, total_income DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Object[] {
                    rs.getString("model"),
                    rs.getInt("times_rented"),
                    rs.getDouble("total_income")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<Object[]> getVehicleAvailabilityReport(java.util.Date date) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT v.model, v.category, v.price_per_day, " +
                     "CASE WHEN v.status = 'Maintenance' THEN 'No' " +
                     "     WHEN EXISTS (SELECT 1 FROM bookings b WHERE b.vehicle_id = v.id " +
                     "                  AND b.status NOT IN ('CANCELLED', 'REJECTED') " +
                     "                  AND ? BETWEEN b.start_date AND b.end_date) THEN 'No' " +
                     "     ELSE 'Yes' END AS available " +
                     "FROM vehicles v ORDER BY v.model";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setDate(1, new java.sql.Date(date.getTime()));
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] {
                        rs.getString("model"),
                        rs.getString("category"),
                        String.format("%,.0f RWF", rs.getDouble("price_per_day")),
                        rs.getString("available")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<Object[]> getBookingsHistoryReport() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT u.full_name, v.model, b.start_date, b.end_date, b.total_cost, b.status " +
                     "FROM bookings b " +
                     "JOIN vehicles v ON b.vehicle_id = v.id " +
                     "JOIN users u ON b.customer_id = u.id " +
                     "WHERE b.status IN ('APPROVED', 'EXPIRED', 'CANCELLED') " +
                     "ORDER BY b.end_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Object[] {
                    rs.getString("full_name"),
                    rs.getString("model"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    String.format("%,.0f RWF", rs.getDouble("total_cost")),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public boolean isDuplicateBooking(int customerId, int vehicleId, java.util.Date startDate, java.util.Date endDate) {
        String sql = "SELECT COUNT(*) FROM bookings " +
                    "WHERE customer_id = ? AND vehicle_id = ? " +
                    "AND start_date = ? AND end_date = ? " +
                    "AND status NOT IN ('REJECTED', 'CANCELLED')";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, customerId);
            pst.setInt(2, vehicleId);
            pst.setDate(3, new java.sql.Date(startDate.getTime()));
            pst.setDate(4, new java.sql.Date(endDate.getTime()));
            
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isVehicleUnavailable(int vehicleId, java.util.Date startDate, java.util.Date endDate) {
        String sql = "SELECT COUNT(*) FROM bookings " +
                    "WHERE vehicle_id = ? " +
                    "AND status NOT IN ('REJECTED', 'CANCELLED') " +
                    "AND (start_date <= ? AND end_date >= ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, vehicleId);
            pst.setDate(2, new java.sql.Date(endDate.getTime()));
            pst.setDate(3, new java.sql.Date(startDate.getTime()));
            
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String getNextAvailableDates(int vehicleId, java.util.Date requestedStart, java.util.Date requestedEnd) {
        String sql = "SELECT start_date, end_date FROM bookings " +
                    "WHERE vehicle_id = ? AND status NOT IN ('CANCELLED', 'REJECTED') " +
                    "AND (start_date <= ? AND end_date >= ?) " +
                    "ORDER BY start_date ASC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, vehicleId);
            pst.setDate(2, new java.sql.Date(requestedEnd.getTime()));
            pst.setDate(3, new java.sql.Date(requestedStart.getTime()));
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    java.util.Date bookedEnd = rs.getDate("end_date");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(bookedEnd);
                    cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                    java.util.Date nextAvailable = cal.getTime();
                    
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    return "Vehicle will be available from " + sdf.format(nextAvailable) + " onwards.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "Please check availability for different dates.";
    }
}