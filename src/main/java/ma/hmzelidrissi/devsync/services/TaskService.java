package ma.hmzelidrissi.devsync.services;

import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {
    Task createTask(Task task, Long assignedUserId);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    void markTaskAsCompleted(Long taskId);
    void replaceTask(Long taskId, User user, Task newTask);
    void deleteTask(Long taskId, User user);
    List<Task> getTasksForUser(Long id);
    List<Task> getOverdueTasks(LocalDate today);
    void updateTask(Task task);
    void replaceTaskByManager(Long oldTaskId, Task newTask, Long newAssigneeId, User manager);
    Map<String, Object> getManagerOverview(Long id);
    void createTaskChangeRequest(Long taskId, User currentUser, Task newTaskDetails);
}
