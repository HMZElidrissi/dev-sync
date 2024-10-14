package ma.hmzelidrissi.devsync.config;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TaskService;
import ma.hmzelidrissi.devsync.services.UserService;

import java.util.Set;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Inject
    private UserService userService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        userService.createDefaultManagerIfNotExists();
        if (userService.getAllUsers().size() == 1) {
            createDefaultUser();
        }
    }

    private void createDefaultUser() {
        User user = new User();
        user.setUsername("ahmed");
        user.setPassword("ahmed");
        user.setFirstName("Ahmed");
        user.setLastName("Ahmadi");
        user.setEmail("ahmed@devsync.com");
        user.setRole(Role.USER);
        userService.createUser(user);
    }
}