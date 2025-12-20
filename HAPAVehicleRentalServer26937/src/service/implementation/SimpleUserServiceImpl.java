package service.implementation;

import dao.SimpleUserDao;
import model.User;
import model.SimpleUser;
import service.UserService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class SimpleUserServiceImpl extends UnicastRemoteObject implements UserService {
    
    private SimpleUserDao userDao;
    
    public SimpleUserServiceImpl() throws RemoteException {
        super();
        this.userDao = new SimpleUserDao();
    }
    
    @Override
    public boolean registerUser(User user) throws RemoteException {
        SimpleUser simpleUser = toSimple(user);
        if (userDao.getUserByUsername(simpleUser.getUsername()) != null) {
            throw new RemoteException("Username already exists");
        }
        if (!isValidEmail(simpleUser.getEmail())) {
            throw new RemoteException("Invalid email format");
        }
        if (!isValidPhone(simpleUser.getPhone())) {
            throw new RemoteException("Invalid phone number format");
        }
        return userDao.saveUser(simpleUser);
    }
    
    @Override
    public boolean updateUser(User user) throws RemoteException {
        SimpleUser simpleUser = toSimple(user);
        SimpleUser existingUser = userDao.getUserById(simpleUser.getId());
        if (existingUser == null) {
            throw new RemoteException("User not found");
        }
        return userDao.updateUser(simpleUser);
    }
    
    @Override
    public boolean deleteUser(int userId) throws RemoteException {
        SimpleUser user = userDao.getUserById(userId);
        if (user != null && "admin".equals(user.getRole())) {
            List<SimpleUser> admins = userDao.getUsersByRole("admin");
            if (admins.size() <= 1) {
                throw new RemoteException("Cannot delete the last admin user");
            }
        }
        return userDao.deleteUser(userId);
    }
    
    @Override
    public User getUserById(int userId) throws RemoteException {
        return toUser(userDao.getUserById(userId));
    }
    
    @Override
    public User authenticateUser(String username, String password) throws RemoteException {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            throw new RemoteException("Username and password are required");
        }
        SimpleUser user = userDao.authenticateUser(username, password);
        if (user != null && !"Active".equals(user.getStatus())) {
            throw new RemoteException("Account is inactive");
        }
        return toUser(user);
    }
    
    @Override
    public List<User> getAllUsers() throws RemoteException {
        List<SimpleUser> simpleUsers = userDao.getAllUsers();
        List<User> users = new ArrayList<>();
        if (simpleUsers != null) {
            for (SimpleUser su : simpleUsers) {
                users.add(toUser(su));
            }
        }
        return users;
    }
    
    @Override
    public List<User> getUsersByRole(String role) throws RemoteException {
        List<SimpleUser> simpleUsers = userDao.getUsersByRole(role);
        List<User> users = new ArrayList<>();
        if (simpleUsers != null) {
            for (SimpleUser su : simpleUsers) {
                users.add(toUser(su));
            }
        }
        return users;
    }
    
    @Override
    public boolean changeUserStatus(int userId, String status) throws RemoteException {
        SimpleUser user = userDao.getUserById(userId);
        if (user != null) {
            user.setStatus(status);
            return userDao.updateUser(user);
        }
        return false;
    }
    
    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws RemoteException {
        SimpleUser user = userDao.getUserById(userId);
        if (user != null && user.getPassword().equals(oldPassword)) {
            if (!isValidPassword(newPassword)) {
                throw new RemoteException("Password must be at least 6 characters long");
            }
            user.setPassword(newPassword);
            return userDao.updateUser(user);
        }
        return false;
    }
    
    private SimpleUser toSimple(User user) {
        if (user == null) return null;
        SimpleUser su = new SimpleUser(user.getUsername(), user.getPassword(), user.getFullName(), 
                                       user.getPhone(), user.getEmail(), user.getRole());
        su.setId(user.getId());
        su.setStatus(user.getStatus());
        return su;
    }
    
    private User toUser(SimpleUser su) {
        if (su == null) return null;
        User user = new User(su.getUsername(), su.getPassword(), su.getFullName(), 
                            su.getPhone(), su.getEmail(), su.getRole());
        user.setId(su.getId());
        user.setStatus(su.getStatus());
        return user;
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9+\\-\\s()]{10,15}$");
    }
    
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}