import ma.hmzelidrissi.devsync.daos.TaskDAO;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TokenService;
import ma.hmzelidrissi.devsync.services.UserService;
import ma.hmzelidrissi.devsync.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setRole(Role.USER);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setDueDate(LocalDate.now().plusDays(1));
        testTask.setTags(new HashSet<>(Arrays.asList("tag1", "tag2")));
        testTask.setAssignedTo(testUser);
        testTask.setCreatedBy(testUser);
        testTask.setCreatedAt(LocalDateTime.now());
        testTask.setCompleted(false);
    }

    @Test
    void createTask_validData_success() {
        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(taskDAO.create(any(Task.class))).thenReturn(testTask);

        Task createdTask = taskService.createTask(testTask, testUser.getId());

        assertNotNull(createdTask);
        assertEquals(testTask.getTitle(), createdTask.getTitle());
        verify(taskDAO).create(any(Task.class));
    }

    @Test
    void createTask_invalidDueDate_throwsException() {
        testTask.setDueDate(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(testTask, testUser.getId()));
    }

    @Test
    void markTaskAsCompleted_success() {
        when(taskDAO.findById(anyLong())).thenReturn(testTask);

        taskService.markTaskAsCompleted(testTask.getId());

        assertTrue(testTask.isCompleted());
        verify(taskDAO).update(testTask);
    }

    @Test
    void replaceTask_byManager_success() {
        User manager = new User();
        manager.setRole(Role.MANAGER);

        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setDueDate(LocalDate.now().plusDays(1));
        newTask.setTags(new HashSet<>(Arrays.asList("tag3", "tag4")));

        when(taskDAO.findById(anyLong())).thenReturn(testTask);

        taskService.replaceTask(testTask.getId(), manager, newTask);

        assertEquals("New Task", testTask.getTitle());
        assertEquals("New Description", testTask.getDescription());
        verify(taskDAO).update(testTask);
        verify(tokenService, never()).useReplaceToken(any(User.class));
    }

    @Test
    void deleteTask_byManager_success() {
        User manager = new User();
        manager.setRole(Role.MANAGER);

        when(taskDAO.findById(anyLong())).thenReturn(testTask);

        taskService.deleteTask(testTask.getId(), manager);

        verify(taskDAO).delete(testTask.getId());
        verify(tokenService, never()).useDeleteToken(any(User.class));
    }

    @Test
    void getManagerOverview_success() {
        User manager = new User();
        manager.setId(2L);
        manager.setRole(Role.MANAGER);

        List<User> employees = Arrays.asList(testUser);
        List<Task> tasks = Arrays.asList(testTask);

        when(userService.getUserById(manager.getId())).thenReturn(manager);
        when(userService.getAllUsers()).thenReturn(employees);
        when(taskDAO.findByAssignedUser(testUser.getId())).thenReturn(tasks);
        when(tokenService.getTokensUsed(testUser.getId())).thenReturn(1);

        Map<String, Object> overview = taskService.getManagerOverview(manager.getId());

        assertNotNull(overview);
        assertTrue(overview.containsKey(testUser.getUsername()));
        verify(userService).getAllUsers();
        verify(taskDAO).findByAssignedUser(testUser.getId());
        verify(tokenService).getTokensUsed(testUser.getId());
    }
}