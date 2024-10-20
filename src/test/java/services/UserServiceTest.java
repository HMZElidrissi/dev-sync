package services;

import ma.hmzelidrissi.devsync.daos.TokenDAO;
import ma.hmzelidrissi.devsync.daos.UserDAO;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TokenDAO tokenDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.USER);
    }

    @Test
    void createUser_success() {
        when(userDAO.create(any(User.class))).thenReturn(testUser);
        when(tokenDAO.create(any(Token.class))).thenReturn(new Token());
        when(userDAO.update(any(User.class))).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        assertNotNull(createdUser);
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertTrue(BCrypt.checkpw("password", createdUser.getPassword()));
        assertNotNull(createdUser.getToken());
        verify(userDAO).create(any(User.class));
        verify(tokenDAO).create(any(Token.class));
        verify(userDAO).update(any(User.class));
    }

    @Test
    void getUserById_success() {
        when(userDAO.findById(1L)).thenReturn(testUser);

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
    }

    @Test
    void getAllUsers_success() {
        List<User> users = Arrays.asList(testUser);
        when(userDAO.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(testUser.getId(), foundUsers.get(0).getId());
    }

    @Test
    void updateUser_success() {
        when(userDAO.update(any(User.class))).thenReturn(testUser);

        User updatedUser = userService.updateUser(testUser);

        assertNotNull(updatedUser);
        assertEquals(testUser.getId(), updatedUser.getId());
        verify(userDAO).update(testUser);
    }

    @Test
    void deleteUser_success() {
        userService.deleteUser(1L);

        verify(userDAO).delete(1L);
    }

    @Test
    void authenticateUser_success() {
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
        testUser.setPassword(hashedPassword);

        when(userDAO.findByUsername("testuser")).thenReturn(testUser);

        User authenticatedUser = userService.authenticateUser("testuser", "password");

        assertNotNull(authenticatedUser);
        assertEquals(testUser.getId(), authenticatedUser.getId());
    }

    @Test
    void authenticateUser_failure() {
        String hashedPassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());
        testUser.setPassword(hashedPassword);

        when(userDAO.findByUsername("testuser")).thenReturn(testUser);

        User authenticatedUser = userService.authenticateUser("testuser", "wrongpassword");

        assertNull(authenticatedUser);
    }

    @Test
    void createDefaultManagerIfNotExists_managerExists() {
        User manager = new User();
        manager.setRole(Role.MANAGER);

        when(userDAO.findByRole(Role.MANAGER)).thenReturn(Arrays.asList(manager));

        userService.createDefaultManagerIfNotExists();

        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    void createDefaultManagerIfNotExists_noManagerExists() {
        when(userDAO.findByRole(Role.MANAGER)).thenReturn(Arrays.asList());
        when(userDAO.create(any(User.class))).thenReturn(new User());
        when(tokenDAO.create(any(Token.class))).thenReturn(new Token());
        when(userDAO.update(any(User.class))).thenReturn(new User());

        userService.createDefaultManagerIfNotExists();

        verify(userDAO).create(any(User.class));
        verify(tokenDAO).create(any(Token.class));
        verify(userDAO).update(any(User.class));
    }
}