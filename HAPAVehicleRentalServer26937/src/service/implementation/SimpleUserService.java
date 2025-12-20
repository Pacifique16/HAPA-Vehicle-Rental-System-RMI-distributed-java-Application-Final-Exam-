package service.implementation;

import dao.SimpleUserDao;
import model.User;
import model.SimpleUser;
import service.UserService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class SimpleUserService extends UnicastRemoteObject implements UserService {
    
    private SimpleUserDao userDao;
    
    public SimpleUserService() throws RemoteException {
        super();
        this.userDao = new SimpleUserDao();
    }
    
    @Override
    public boolean registerUser(User user) throws RemoteException {
        SimpleUser simpleUser = toSimple(user);
        if (userDao.getUserByUsername(simpleUser.getUsername()) != null) {
            throw new RemoteException("Username already exists");
        }
        return userDao.saveUser(simpleUser);
    }
    
    @Override
    public boolean updateUser(User user) throws RemoteException {
        SimpleUser simpleUser = toSimple(user);
        return userDao.updateUser(simpleUser);
    }
    
    @Override
    public boolean deleteUser(int userId) throws RemoteException {
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
}