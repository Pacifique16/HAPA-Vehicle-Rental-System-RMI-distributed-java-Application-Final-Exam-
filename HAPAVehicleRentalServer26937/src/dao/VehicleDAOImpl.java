package dao;

import model.Vehicle;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.ArrayList;

public class VehicleDAOImpl implements VehicleDAO {
    
    @Override
    public boolean addVehicle(Vehicle vehicle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(vehicle);
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
    public List<Vehicle> getAllVehicles() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Vehicle> query = session.createQuery("FROM Vehicle", Vehicle.class);
            List<Vehicle> results = query.list();
            return results != null ? results : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error getting all vehicles: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public Vehicle findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Vehicle.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(vehicle);
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
    public boolean deleteVehicle(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, id);
            if (vehicle != null) {
                session.delete(vehicle);
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
    public List<Vehicle> searchVehicles(String query) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Vehicle> q = session.createQuery(
                "FROM Vehicle WHERE make LIKE :search OR model LIKE :search", Vehicle.class);
            q.setParameter("search", "%" + query + "%");
            return q.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public int countVehicles() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Vehicle", Long.class);
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.close();
        }
    }
    
    @Override
    public int countAvailableToday() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Vehicle WHERE status = 'Available'", Long.class);
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateVehicleStatus(int vehicleId, String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, vehicleId);
            if (vehicle != null) {
                vehicle.setStatus(status);
                session.update(vehicle);
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
    public boolean isVehicleAvailableForBooking(int vehicleId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Vehicle vehicle = session.get(Vehicle.class, vehicleId);
            return vehicle != null && "Available".equals(vehicle.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}