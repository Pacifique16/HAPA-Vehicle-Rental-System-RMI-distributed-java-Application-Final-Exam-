package dao;

import model.SimpleUser;
import java.util.*;

public class SimpleUserDao {
    private static Map<Integer, SimpleUser> users = new HashMap<>();
    private static int nextId = 1;
    
    static {
        // Add sample users
        SimpleUser admin = new SimpleUser("admin", "admin123", "System Administrator", "+250788123456", "admin@hapa.com", "admin");
        admin.setId(nextId++);
        admin.setStatus("Active");
        users.put(admin.getId(), admin);
        
        SimpleUser customer = new SimpleUser("john_doe", "password123", "John Doe", "+250788654321", "john@email.com", "customer");
        customer.setId(nextId++);
        customer.setStatus("Active");
        users.put(customer.getId(), customer);
    }
    
    public boolean saveUser(SimpleUser user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return true;
    }
    
    public boolean updateUser(SimpleUser user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return true;
        }
        return false;
    }
    
    public boolean deleteUser(int userId) {
        return users.remove(userId) != null;
    }
    
    public SimpleUser getUserById(int userId) {
        return users.get(userId);
    }
    
    public SimpleUser getUserByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    public SimpleUser authenticateUser(String username, String password) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
    
    public List<SimpleUser> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public List<SimpleUser> getUsersByRole(String role) {
        return users.values().stream()
                .filter(u -> u.getRole().equals(role))
                .collect(ArrayList::new, (list, user) -> list.add(user), ArrayList::addAll);
    }
}