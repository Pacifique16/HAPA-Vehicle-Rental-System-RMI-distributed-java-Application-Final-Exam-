package service;

import model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserService extends Remote {
    User authenticateUser(String username, String password) throws RemoteException;
    boolean addUser(User user) throws RemoteException;
    boolean updateUser(User user) throws RemoteException;
    boolean deleteUser(int id) throws RemoteException;
    User findById(int id) throws RemoteException;
    List<User> getAllUsers() throws RemoteException;
    boolean changePassword(int id, String oldP, String newP) throws RemoteException;
    boolean updateUserProfile(User user) throws RemoteException;
    int countUsers() throws RemoteException;
    boolean updateUserStatus(int userId, String status) throws RemoteException;
}