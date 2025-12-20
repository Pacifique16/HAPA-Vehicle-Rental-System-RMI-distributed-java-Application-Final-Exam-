package dao;

import model.User;
import java.util.List;

public interface UserDAO {
    User login(String username, String password);
    boolean addUser(User user);
    boolean updateUserProfile(User user);
    boolean changePassword(int id, String oldP, String newP);
    List<User> getAllUsers();
    int countUsers();
    boolean deleteUser(int id);
    boolean updateUser(User user);
    User findById(int id);
    User authenticateUser(String username, String password);
    boolean updateUserStatus(int userId, String status);
}