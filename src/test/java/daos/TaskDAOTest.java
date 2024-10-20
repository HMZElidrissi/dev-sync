package daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ma.hmzelidrissi.devsync.daos.impl.TaskDAOImpl;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.TaskChangeRequest;
import ma.hmzelidrissi.devsync.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Task> typedQuery;

    @InjectMocks
    private TaskDAOImpl taskDAO;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("test task");
        testTask.setDescription("test description");
        testTask.setDueDate(LocalDate.now().plusDays(1));
        testTask.setTags(Set.of("tag1", "tag2"));
        testTask.setCompleted(false);
    }

    @Test
    void create_shouldPersistTask() {
        taskDAO.create(testTask);
        verify(entityManager).persist(testTask);
    }

    @Test
    void findById_shouldReturnTask() {
        when(entityManager.find(Task.class, 1L)).thenReturn(testTask);
        Task task = taskDAO.findById(1L);
        assertEquals(testTask, task);
    }

    @Test
    void findAll_shouldReturnListOfTasks() {
        List<Task> tasks = List.of(testTask, new Task());
        when(entityManager.createQuery("SELECT t FROM Task t", Task.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(tasks);

        List<Task> results = taskDAO.findAll();

        assertEquals(2, results.size());
        assertEquals(tasks, results);
    }

    @Test
    void update_shouldMergeTask() {
        when(entityManager.merge(testTask)).thenReturn(testTask);

        Task result = taskDAO.update(testTask);

        assertEquals(testTask, result);

        verify(entityManager).merge(testTask);
    }

    @Test
    void delete_shouldRemoveTask() {
        when(entityManager.find(Task.class, 1L)).thenReturn(testTask);

        taskDAO.delete(1L);

        verify(entityManager).remove(testTask);
    }

    @Test
    void findByUserId_shouldReturnListOfTasks() {

        List<Task> tasks = List.of(testTask);

        when(entityManager.createQuery("SELECT t FROM Task t WHERE t.assignedTo.id = :id", Task.class)).thenReturn(typedQuery);

        when(typedQuery.setParameter("id", 1L)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(tasks);

        List<Task> results = taskDAO.findByUserId(1L);

        assertEquals(1, results.size());
        assertEquals(tasks, results);
    }

    @Test
    void findOverdueTasks_shouldReturnListOfTasks() {
        List<Task> tasks = List.of(testTask);

        when(entityManager.createQuery("SELECT t FROM Task t WHERE t.dueDate < :today AND t.completed = false", Task.class)).thenReturn(typedQuery);

        when(typedQuery.setParameter("today", LocalDate.now())).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(tasks);

        List<Task> results = taskDAO.findOverdueTasks(LocalDate.now());

        assertEquals(results, tasks);
    }

    @Test
    void findByAssignedUser_shouldReturnListOfTasks() {
        List<Task> tasks = List.of(testTask);

        when(entityManager.createQuery("SELECT t FROM Task t WHERE t.assignedTo.id = :userId", Task.class)).thenReturn(typedQuery);

        when(typedQuery.setParameter("userId", 1L)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(tasks);

        List<Task> results = taskDAO.findByAssignedUser(1L);

        assertEquals(1, results.size());
        assertEquals(tasks, results);
    }

    @Test
    void createTaskChangeRequest_shouldPersistTaskChangeRequest() {
        TaskChangeRequest request = new TaskChangeRequest();
        request.setTask(testTask);
        request.setRequestor(new User());
        request.setRequestTime(LocalDateTime.now());
        request.setAnswered(false);

        taskDAO.createTaskChangeRequest(request);

        verify(entityManager).persist(request);
    }
}
