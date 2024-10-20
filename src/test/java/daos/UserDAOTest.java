package daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ma.hmzelidrissi.devsync.daos.impl.UserDAOImpl;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> typedQuery;

    @InjectMocks
    private UserDAOImpl userDAO;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.USER);
    }

    /**
     * we test that the create method persists the user through the entity manager
     * by verifying that the persist method is called with the test user as an argument, because the create method calls the persist method on the entity manager with the user as an argument.
     */
    @Test
    void create_shouldPersistUser() {
        userDAO.create(testUser);
        verify(entityManager).persist(testUser);
    }

    /**
     * we test that the findById method returns the user with the given id by mocking the find method of the entity manager to return the test user when called with the id 1.
     */
    @Test
    void findById_shouldReturnUser() {
        when(entityManager.find(User.class, 1L)).thenReturn(testUser);
        User result = userDAO.findById(1L);
        assertEquals(testUser, result);
    }

    /**
     * we test that the findAll method returns a list of users by mocking the getResultList method of the typed query to return a list of users when called.
     */
    @Test
    void findAll_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(testUser, new User());
        when(entityManager.createQuery("SELECT u FROM User u", User.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(users);

        List<User> result = userDAO.findAll();
        assertEquals(2, result.size());
        assertEquals(users, result);
    }

    /**
     * we test that the update method merges the user by verifying that the merge method is called with the test user as an argument, because the update method calls the merge method on the entity manager with the user as an argument.
     */
    @Test
    void update_shouldMergeUser() {
        when(entityManager.merge(testUser)).thenReturn(testUser);
        User result = userDAO.update(testUser);
        assertEquals(testUser, result);
        verify(entityManager).merge(testUser);
    }

    /**
     * we test that the delete method removes the user if it exists by mocking the find method of the entity manager to return the test user when called with the id 1.
     */
    @Test
    void delete_shouldRemoveUserIfExists() {
        when(entityManager.find(User.class, 1L)).thenReturn(testUser);
        userDAO.delete(1L);
        verify(entityManager).remove(testUser);
    }

    /**
     * we test that the delete method does not remove the user if it does not exist by mocking the find method of the entity manager to return null when called with the id 1.
     */
    @Test
    void delete_shouldNotRemoveUserIfNotExists() {
        when(entityManager.find(User.class, 1L)).thenReturn(null);
        userDAO.delete(1L);
        verify(entityManager, never()).remove(any(User.class));
    }

    /**
     * we test that the findByUsername method returns the user with the given username by mocking the getSingleResult method of the typed query to return the test user when called.
     */
    @Test
    void findByUsername_shouldReturnUser() {
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter("username", "testuser")).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(testUser);

        User result = userDAO.findByUsername("testuser");
        assertEquals(testUser, result);
    }

    /**
     * we test that the findByRole method returns a list of users with the given role by mocking the getResultList method of the typed query to return a list of users when called.
     */
    @Test
    void findByRole_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(testUser, new User());
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter("role", Role.USER)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(users);

        List<User> result = userDAO.findByRole(Role.USER);
        assertEquals(2, result.size());
        assertEquals(users, result);
    }
}
