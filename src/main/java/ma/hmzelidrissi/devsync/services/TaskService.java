package ma.hmzelidrissi.devsync.services;

import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.User;

import java.util.List;

public interface TaskService {
    Task createTask(Task task, Long assignedUserId);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    void markTaskAsCompleted(Long taskId);
    void replaceTask(Long taskId, User user, Task newTask);
    void deleteTask(Long taskId, User user);
}
