package dao;

import model.Booking;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

public class BookingDAOImpl implements BookingDAO {
    
    @Override
    public boolean saveBooking(Booking booking) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(booking);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Booking> getAllBookings() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Booking> query = session.createQuery("FROM Booking", Booking.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Booking> query = session.createQuery("FROM Booking b WHERE b.customerId = :customerId", Booking.class);
            query.setParameter("customerId", customerId);
            List<Booking> results = query.list();
            
            // Debug logging for rejection reasons
            for (Booking b : results) {
                if ("Rejected".equals(b.getStatus()) || "REJECTED".equals(b.getStatus())) {
                    System.out.println("DEBUG SERVER: Booking " + b.getId() + " status=" + b.getStatus() + " reason='" + b.getRejectionReason() + "'");
                }
            }
            
            return results != null ? results : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error getting bookings for customer " + customerId + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public Booking getBookingById(int bookingId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Booking.class, bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateBooking(Booking booking) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(booking);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean deleteBooking(int bookingId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                session.delete(booking);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean cancelBooking(int bookingId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                booking.setStatus("CANCELLED");
                session.update(booking);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean reopenBooking(int bookingId, Date newStartDate, Date newEndDate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                booking.setStatus("PENDING");
                booking.setStartDate(newStartDate);
                booking.setEndDate(newEndDate);
                session.update(booking);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    // Simplified implementations for remaining methods
    @Override
    public List<String[]> getTodaysBookingsWithDetails() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Get only today's bookings
            String hql = "SELECT b FROM Booking b WHERE DATE(b.startDate) = CURRENT_DATE OR DATE(b.endDate) = CURRENT_DATE OR (b.startDate <= CURRENT_DATE AND b.endDate >= CURRENT_DATE)";
            Query<Booking> query = session.createQuery(hql, Booking.class);
            List<Booking> bookings = query.list();
            
            List<String[]> bookingDetails = new ArrayList<>();
            for (Booking b : bookings) {
                try {
                    // Get user details
                    Query<String> userQuery = session.createQuery("SELECT u.fullName FROM User u WHERE u.id = :userId", String.class);
                    userQuery.setParameter("userId", b.getCustomerId());
                    String customerName = userQuery.uniqueResult();
                    
                    // Get vehicle details
                    Query<String> vehicleQuery = session.createQuery("SELECT v.model FROM Vehicle v WHERE v.id = :vehicleId", String.class);
                    vehicleQuery.setParameter("vehicleId", b.getVehicleId());
                    String vehicleModel = vehicleQuery.uniqueResult();
                    
                    String[] booking = new String[6];
                    booking[0] = customerName != null ? customerName : "Unknown Customer";
                    booking[1] = vehicleModel != null ? vehicleModel : "Unknown Vehicle";
                    booking[2] = b.getStartDate() != null ? b.getStartDate().toString() : "N/A";
                    booking[3] = b.getEndDate() != null ? b.getEndDate().toString() : "N/A";
                    booking[4] = b.getStatus() != null ? b.getStatus() : "Pending";
                    booking[5] = b.getTotalCost() > 0 ? b.getTotalCost() + " RWF" : "0 RWF";
                    bookingDetails.add(booking);
                } catch (Exception e) {
                    System.err.println("Error processing booking: " + e.getMessage());
                }
            }
            
            System.out.println("Found " + bookingDetails.size() + " bookings for today");
            return bookingDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public int[] getDashboardStats() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Get total vehicles count
            Query<Long> vehicleQuery = session.createQuery("SELECT COUNT(*) FROM Vehicle", Long.class);
            int totalVehicles = vehicleQuery.uniqueResult().intValue();
            System.out.println("DEBUG: Total vehicles = " + totalVehicles);
            
            // Get total users count
            Query<Long> userQuery = session.createQuery("SELECT COUNT(*) FROM User", Long.class);
            int totalUsers = userQuery.uniqueResult().intValue();
            System.out.println("DEBUG: Total users = " + totalUsers);
            
            // Get total bookings count
            Query<Long> bookingQuery = session.createQuery("SELECT COUNT(*) FROM Booking", Long.class);
            int totalBookings = bookingQuery.uniqueResult().intValue();
            System.out.println("DEBUG: Total bookings = " + totalBookings);
            
            // Get available vehicles count (not currently booked today)
            Query<Long> availableQuery = session.createQuery(
                "SELECT COUNT(DISTINCT v.id) FROM Vehicle v WHERE v.id NOT IN " +
                "(SELECT DISTINCT b.vehicleId FROM Booking b WHERE b.status IN ('APPROVED', 'PENDING') " +
                "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate)", Long.class);
            int availableVehicles = availableQuery.uniqueResult().intValue();
            System.out.println("DEBUG: Available vehicles (not booked today) = " + availableVehicles);
            
            int[] stats = new int[]{totalVehicles, totalUsers, totalBookings, availableVehicles};
            System.out.println("DEBUG: Returning stats = " + java.util.Arrays.toString(stats));
            return stats;
        } catch (Exception e) {
            System.err.println("ERROR in getDashboardStats: " + e.getMessage());
            e.printStackTrace();
            return new int[]{0, 0, 0, 0};
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Object[]> getPendingBookingsReport() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Get only PENDING bookings
            Query<Booking> query = session.createQuery("FROM Booking WHERE status = 'PENDING'", Booking.class);
            List<Booking> bookings = query.list();
            
            List<Object[]> report = new ArrayList<>();
            for (Booking b : bookings) {
                try {
                    // Get customer details
                    Query<Object[]> customerQuery = session.createQuery(
                        "SELECT u.fullName, u.phone FROM User u WHERE u.id = :userId", Object[].class);
                    customerQuery.setParameter("userId", b.getCustomerId());
                    Object[] customerData = customerQuery.uniqueResult();
                    
                    // Get vehicle details
                    Query<Object[]> vehicleQuery = session.createQuery(
                        "SELECT v.model, v.plateNumber FROM Vehicle v WHERE v.id = :vehicleId", Object[].class);
                    vehicleQuery.setParameter("vehicleId", b.getVehicleId());
                    Object[] vehicleData = vehicleQuery.uniqueResult();
                    
                    Object[] row = new Object[9];
                    row[0] = b.getId();
                    row[1] = customerData != null ? customerData[0] : "Unknown Customer";
                    row[2] = customerData != null ? customerData[1] : "N/A";
                    row[3] = vehicleData != null ? vehicleData[0] : "Unknown Vehicle";
                    row[4] = vehicleData != null ? vehicleData[1] : "N/A";
                    row[5] = b.getStartDate() != null ? b.getStartDate().toString() : "N/A";
                    row[6] = b.getEndDate() != null ? b.getEndDate().toString() : "N/A";
                    row[7] = b.getTotalCost() + " RWF";
                    row[8] = b.getStatus();
                    
                    report.add(row);
                } catch (Exception e) {
                    System.err.println("Error processing booking " + b.getId() + ": " + e.getMessage());
                }
            }
            
            System.out.println("Found " + report.size() + " pending bookings for approval panel");
            return report;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean approveBooking(int bookingId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                booking.setStatus("APPROVED");
                session.update(booking);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean rejectBooking(int bookingId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                booking.setStatus("REJECTED");
                session.update(booking);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean rejectBookingWithReason(int bookingId, String reason) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                booking.setStatus("REJECTED");
                booking.setRejectionReason(reason);
                session.update(booking);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Object[]> getActiveRentalsReport() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT u.fullName, v.model, b.startDate, b.endDate, b.totalCost, b.status " +
                        "FROM Booking b, User u, Vehicle v " +
                        "WHERE b.customerId = u.id AND b.vehicleId = v.id " +
                        "AND b.status = 'APPROVED' " +
                        "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Object[]> getMostRentedVehicles() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT v.model, COUNT(b.id) as times_rented, COALESCE(SUM(b.totalCost), 0) as total_income " +
                        "FROM Vehicle v LEFT JOIN Booking b ON v.id = b.vehicleId " +
                        "GROUP BY v.id, v.model " +
                        "ORDER BY times_rented DESC";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Object[]> getVehicleAvailabilityReport(Date date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT v.model, v.category, v.pricePerDay, " +
                        "CASE WHEN EXISTS (SELECT 1 FROM Booking b WHERE b.vehicleId = v.id " +
                        "AND b.status = 'APPROVED' AND :date BETWEEN b.startDate AND b.endDate) " +
                        "THEN 'No' ELSE 'Yes' END as available " +
                        "FROM Vehicle v";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("date", date);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Object[]> getBookingsHistoryReport() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT u.fullName, v.model, b.startDate, b.endDate, b.totalCost, b.status " +
                        "FROM Booking b, User u, Vehicle v " +
                        "WHERE b.customerId = u.id AND b.vehicleId = v.id " +
                        "ORDER BY b.bookingDate DESC";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean isDuplicateBooking(int customerId, int vehicleId, Date startDate, Date endDate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Booking WHERE customerId = :customerId AND vehicleId = :vehicleId " +
                "AND startDate = :startDate AND endDate = :endDate", Long.class);
            query.setParameter("customerId", customerId);
            query.setParameter("vehicleId", vehicleId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean isVehicleUnavailable(int vehicleId, Date startDate, Date endDate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Booking WHERE vehicleId = :vehicleId " +
                "AND status IN ('PENDING', 'APPROVED') " +
                "AND ((startDate <= :endDate AND endDate >= :startDate))", Long.class);
            query.setParameter("vehicleId", vehicleId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public String getNextAvailableDates(int vehicleId, Date requestedStart, Date requestedEnd) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Get all conflicting bookings for this vehicle
            Query<Booking> query = session.createQuery(
                "FROM Booking WHERE vehicleId = :vehicleId " +
                "AND status IN ('PENDING', 'APPROVED') " +
                "AND ((startDate <= :endDate AND endDate >= :startDate)) " +
                "ORDER BY startDate", Booking.class);
            query.setParameter("vehicleId", vehicleId);
            query.setParameter("startDate", requestedStart);
            query.setParameter("endDate", requestedEnd);
            
            List<Booking> conflictingBookings = query.list();
            
            if (conflictingBookings.isEmpty()) {
                return "No conflicts found";
            }
            
            StringBuilder message = new StringBuilder();
            message.append("Vehicle is booked during:\n");
            
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            
            for (Booking booking : conflictingBookings) {
                message.append("• ").append(dateFormat.format(booking.getStartDate()))
                       .append(" to ").append(dateFormat.format(booking.getEndDate()))
                       .append(" (").append(booking.getStatus()).append(")\n");
            }
            
            // Find next available date after the last conflicting booking
            Date lastEndDate = null;
            for (Booking booking : conflictingBookings) {
                if (lastEndDate == null || booking.getEndDate().after(lastEndDate)) {
                    lastEndDate = booking.getEndDate();
                }
            }
            
            if (lastEndDate != null) {
                // Add one day to the last end date
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(lastEndDate);
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                Date nextAvailable = cal.getTime();
                
                message.append("\nVehicle will be available from: ")
                       .append(dateFormat.format(nextAvailable));
            }
            
            return message.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error checking availability";
        } finally {
            session.close();
        }
    }
}