package dao;

import model.User;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {
    
    @Override
    public boolean saveUser(User user) {
        return addUser(user);
    }
    
    @Override
    public User getUserById(int id) {
        return findById(id);
    }
    
    @Override
    public User getUserByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    @Override
    public User getUserByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean isDuplicateUsername(String username) {
        return isUsernameExists(username);
    }
    
    @Override
    public boolean isDuplicateEmail(String email) {
        return isEmailExists(email);
    }
    
    // Legacy methods for backward compatibility
    public User login(String username, String password) {
        return authenticateUser(username, password);
    }
    
    @Override
    public User authenticateUser(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User WHERE username = :username AND password = :password AND (status = 'Active' OR status IS NULL)", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    public boolean addUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            System.out.println("=== USER REGISTRATION DEBUG ===");
            System.out.println("Attempting to register user: " + user.getUsername() + ", " + user.getEmail());
            System.out.println("Full name: " + user.getFullName());
            System.out.println("Phone: " + user.getPhone());
            System.out.println("Role: " + user.getRole());
            
            // Check for duplicate username
            if (isUsernameExists(user.getUsername())) {
                System.out.println("ERROR: Username already exists: " + user.getUsername());
                return false;
            }
            
            // Check for duplicate email
            if (isEmailExists(user.getEmail())) {
                System.out.println("ERROR: Email already exists: " + user.getEmail());
                return false;
            }
            
            System.out.println("No duplicates found. Saving user to database...");
            session.save(user);
            transaction.commit();
            System.out.println("SUCCESS: User registered successfully: " + user.getUsername());
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("ERROR: Failed to register user: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> results = query.list();
            return results != null ? results : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(user);
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
    public boolean deleteUser(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
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
    
    public User findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    public boolean changePassword(int id, String oldP, String newP) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null && user.getPassword().equals(oldP)) {
                user.setPassword(newP);
                session.update(user);
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
    public boolean updateUserProfile(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            System.out.println("=== USER PROFILE UPDATE DEBUG ===");
            System.out.println("Updating user ID: " + user.getId());
            System.out.println("New email: " + user.getEmail());
            System.out.println("New phone: " + user.getPhone());
            
            // Check if email exists for OTHER users (not this user)
            Query<Long> emailQuery = session.createQuery(
                "SELECT COUNT(*) FROM User WHERE email = :email AND id != :id", Long.class);
            emailQuery.setParameter("email", user.getEmail());
            emailQuery.setParameter("id", user.getId());
            
            if (emailQuery.uniqueResult() > 0) {
                System.out.println("ERROR: Email already exists for another user");
                return false;
            }
            
            System.out.println("No email conflicts. Updating user profile...");
            session.update(user);
            transaction.commit();
            System.out.println("SUCCESS: User profile updated successfully");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("ERROR: Failed to update user profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    public int countUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM User", Long.class);
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean updateUserStatus(int userId, String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                user.setStatus(status);
                session.update(user);
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
    
    public boolean isUsernameExists(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User WHERE username = :username", Long.class);
            query.setParameter("username", username);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    public boolean isEmailExists(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM User WHERE email = :email", Long.class);
            query.setParameter("email", email);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}