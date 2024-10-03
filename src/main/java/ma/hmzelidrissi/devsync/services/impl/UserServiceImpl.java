package ma.hmzelidrissi.devsync.services.impl;

import ma.hmzelidrissi.devsync.daos.UserDAO;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class UserServiceImpl implements UserService {

    @Inject
    private UserDAO userDAO;

    @Override
    public User createUser(User user) {
        return userDAO.create(user);
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
}