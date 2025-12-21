package dao;

import model.User;
import java.util.List;

public interface UserDAO {
    boolean saveUser(User user);
    User getUserById(int id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
    User authenticateUser(String username, String password);
    boolean updateUserProfile(User user);
    boolean isDuplicateUsername(String username);
    boolean isDuplicateEmail(String email);
    boolean updateUserStatus(int userId, String status);
}