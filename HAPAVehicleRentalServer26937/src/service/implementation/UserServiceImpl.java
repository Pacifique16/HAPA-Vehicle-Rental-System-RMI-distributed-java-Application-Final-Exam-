package service.implementation;

import dao.UserDAOImpl;
import model.User;
import service.UserService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {
    
    private UserDAOImpl userDAO;
    
    public UserServiceImpl() throws RemoteException {
        super();
        try {
            this.userDAO = new UserDAOImpl();
            System.out.println("UserServiceImpl created successfully");
        } catch (Exception e) {
            System.err.println("Error creating UserServiceImpl: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Failed to initialize UserService", e);
        }
    }
    
    @Override
    public User authenticateUser(String username, String password) throws RemoteException {
        return userDAO.authenticateUser(username, password);
    }
    
    @Override
    public boolean addUser(User user) throws RemoteException {
        return userDAO.addUser(user);
    }
    
    @Override
    public boolean updateUser(User user) throws RemoteException {
        return userDAO.updateUser(user);
    }
    
    @Override
    public boolean deleteUser(int id) throws RemoteException {
        return userDAO.deleteUser(id);
    }
    
    @Override
    public User findById(int id) throws RemoteException {
        return userDAO.findById(id);
    }
    
    @Override
    public List<User> getAllUsers() throws RemoteException {
        return userDAO.getAllUsers();
    }
    
    @Override
    public boolean changePassword(int id, String oldP, String newP) throws RemoteException {
        return userDAO.changePassword(id, oldP, newP);
    }
    
    @Override
    public boolean updateUserProfile(User user) throws RemoteException {
        return userDAO.updateUserProfile(user);
    }
    
    @Override
    public int countUsers() throws RemoteException {
        return userDAO.countUsers();
    }
    
    @Override
    public boolean updateUserStatus(int userId, String status) throws RemoteException {
        return userDAO.updateUserStatus(userId, status);
    }
}