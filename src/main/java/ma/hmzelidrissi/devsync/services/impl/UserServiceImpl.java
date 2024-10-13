package ma.hmzelidrissi.devsync.services.impl;

import ma.hmzelidrissi.devsync.daos.TokenDAO;
import ma.hmzelidrissi.devsync.daos.UserDAO;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@ApplicationScoped
@Transactional
public class UserServiceImpl implements UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private TokenDAO tokenDAO;

    @Override
    public User createUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        User createdUser = userDAO.create(user);

        Token token = new Token();
        token.setUser(createdUser);
        token = tokenDAO.create(token);

        createdUser.setToken(token);
        createdUser = userDAO.update(createdUser);

        return createdUser;
    }

    @Override
    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userDAO.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.delete(id);
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public void createDefaultManagerIfNotExists() {
        List<User> managers = userDAO.findByRole(Role.MANAGER);
        if (managers.isEmpty()) {
            User defaultManager = new User();
            defaultManager.setUsername("admin");
            defaultManager.setPassword("admin123");
            defaultManager.setFirstName("Admin");
            defaultManager.setLastName("User");
            defaultManager.setEmail("admin@devsync.com");
            defaultManager.setRole(Role.MANAGER);
            createUser(defaultManager);
        }
    }
}