package dao;

import model.Maintenance;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class MaintenanceDao {
    
    public boolean saveMaintenance(Maintenance maintenance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(maintenance);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateMaintenance(Maintenance maintenance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(maintenance);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteMaintenance(int maintenanceId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Maintenance maintenance = session.get(Maintenance.class, maintenanceId);
            if (maintenance != null) {
                session.delete(maintenance);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    
    public Maintenance getMaintenanceById(int maintenanceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Maintenance.class, maintenanceId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Maintenance> getAllMaintenance() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Maintenance", Maintenance.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Maintenance> getMaintenanceByVehicleId(int vehicleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Maintenance> query = session.createQuery(
                "SELECT m FROM Maintenance m JOIN m.vehicles v WHERE v.id = :vehicleId", 
                Maintenance.class);
            query.setParameter("vehicleId", vehicleId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}