package ma.hmzelidrissi.devsync.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TaskDAO;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TaskService;
import ma.hmzelidrissi.devsync.services.TokenService;
import ma.hmzelidrissi.devsync.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class TaskServiceImpl implements TaskService {

    @Inject
    private TaskDAO taskDAO;

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    public Task createTask(Task task, Long assignedUserId) {
        validateTask(task);
        User assignedUser = userService.getUserById(assignedUserId);
        task.setAssignedTo(assignedUser);
        task.setCreatedAt(LocalDateTime.now());
        return taskDAO.create(task);
    }

    public Task getTaskById(Long id) {
        return taskDAO.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    public void markTaskAsCompleted(Long taskId) {
        Task task = taskDAO.findById(taskId);
        if (task != null) {
            if (LocalDateTime.now().isAfter(task.getDueDate())) {
                throw new IllegalStateException("Task cannot be marked as completed after its due date");
            }
            task.setCompleted(true);
            taskDAO.update(task);
        }
    }

    public void replaceTask(Long taskId, User user, Task newTask) {
        Task existingTask = taskDAO.findById(taskId);
        if (existingTask.getAssignedTo().getRole() == Role.MANAGER && !existingTask.getCreatedBy().equals(user)) {
            tokenService.useReplaceToken(user);
        }
        validateTask(newTask);
        existingTask.setTitle(newTask.getTitle());
        existingTask.setDescription(newTask.getDescription());
        existingTask.setDueDate(newTask.getDueDate());
        existingTask.setTags(newTask.getTags());
        taskDAO.update(existingTask);
    }

    public void deleteTask(Long taskId, User user) {
        Task task = taskDAO.findById(taskId);
        if (!task.getCreatedBy().equals(user)) {
            tokenService.useDeleteToken(user);
        }
        taskDAO.delete(taskId);
    }

    private void validateTask(Task task) {
        LocalDateTime now = LocalDateTime.now();

        if (task.getDueDate().isBefore(now)) {
            throw new IllegalArgumentException("Task cannot be created in the past");
        }

        if (task.getDueDate().isAfter(now.plusDays(3))) {
            throw new IllegalArgumentException("Task cannot be scheduled more than 3 days in advance");
        }

        if (task.getTags() == null || task.getTags().size() < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }
    }
}
