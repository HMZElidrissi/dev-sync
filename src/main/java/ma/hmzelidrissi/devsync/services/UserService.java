package ma.hmzelidrissi.devsync.services;

import ma.hmzelidrissi.devsync.entities.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
    User authenticateUser(String username, String password);
    void createDefaultManagerIfNotExists();
}