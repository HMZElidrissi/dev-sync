package ma.hmzelidrissi.devsync.daos.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import ma.hmzelidrissi.devsync.daos.UserDAO;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.User;

import java.util.List;

@Alternative
public class UserDAOImplWithoutTransactional implements UserDAO {
    private EntityManager entityManager;

    // Constructor injection instead of @PersistenceContext
    @Inject
    public UserDAOImplWithoutTransactional(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User update(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User updatedUser = entityManager.merge(user);
            transaction.commit();
            return updatedUser;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = findById(id);
            if (user != null) {
                entityManager.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public List<User> findByRole(Role role) {
        return List.of();
    }
}