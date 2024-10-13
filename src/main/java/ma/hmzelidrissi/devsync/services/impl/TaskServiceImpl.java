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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskServiceImpl.class.getName());

    @Inject
    private TaskDAO taskDAO;

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    public Task createTask(Task task, Long assignedUserId) {
        validateTask(task);
        User assignedUser = userService.getUserById(assignedUserId);
        if (assignedUser == null) {
            throw new IllegalArgumentException("Assigned user not found");
        }
        task.setAssignedTo(assignedUser);
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
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
            if (LocalDate.now().isAfter(task.getDueDate())) {
                throw new IllegalStateException("Task cannot be marked as completed after its due date");
            }
            task.setCompleted(true);
            taskDAO.update(task);
        }
    }

    public void replaceTask(Long taskId, User user, Task newTask) {
        Task existingTask = taskDAO.findById(taskId);
        if (user.getRole() != Role.MANAGER) {
            if (!existingTask.getCreatedBy().equals(user)) {
                tokenService.useReplaceToken(user);
            }
        }
        validateTask(newTask);
        existingTask.setTitle(newTask.getTitle());
        existingTask.setDescription(newTask.getDescription());
        existingTask.setDueDate(newTask.getDueDate());
        existingTask.setTags(newTask.getTags());
        taskDAO.update(existingTask);
    }

    public void deleteTask(Long taskId, User user) {
        LOGGER.log(Level.INFO, "Attempting to delete task with ID: {0} by user: {1}", new Object[]{taskId, user.getUsername()});
        Task task = taskDAO.findById(taskId);
        if (task == null) {
            LOGGER.log(Level.WARNING, "Task not found with ID: {0}", taskId);
            throw new IllegalArgumentException("Task not found");
        }

        LOGGER.log(Level.INFO, "Task found. Created by: {0}, Assigned to: {1}", new Object[]{task.getCreatedBy().getUsername(), task.getAssignedTo().getUsername()});

        try {
            if (user.getRole() != Role.MANAGER) {
                if (!task.getCreatedBy().getId().equals(user.getId()) && !task.getAssignedTo().getId().equals(user.getId())) {
                    LOGGER.log(Level.WARNING, "User {0} attempted to delete task {1} without permission", new Object[]{user.getUsername(), taskId});
                    throw new IllegalStateException("You don't have permission to delete this task");
                }
                if (!task.getCreatedBy().getId().equals(user.getId())) {
                    LOGGER.log(Level.INFO, "Attempting to use delete token for user {0}", user.getUsername());
                    try {
                        tokenService.useDeleteToken(user);
                        LOGGER.log(Level.INFO, "Delete token successfully used for user {0}", user.getUsername());
                    } catch (RuntimeException e) {
                        LOGGER.log(Level.SEVERE, "Error using delete token: " + e.getMessage(), e);
                        throw new IllegalStateException("Unable to use delete token: " + e.getMessage());
                    }
                }
            }

            LOGGER.log(Level.INFO, "Deleting task with ID: {0}", taskId);
            taskDAO.delete(taskId);
            LOGGER.log(Level.INFO, "Task deleted successfully");
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "Error deleting task: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during task deletion: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while deleting the task", e);
        }
    }

    @Override
    public List<Task> getTasksForUser(Long id) {
        return taskDAO.findByUserId(id);
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (task.getDueDate() == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (task.getTags() == null || task.getTags().isEmpty()) {
            throw new IllegalArgumentException("Tags cannot be null or empty");
        }

        LocalDate now = LocalDate.now();

        if (task.getDueDate().isBefore(now)) {
            throw new IllegalArgumentException("Task cannot be created in the past");
        }

        if (task.getDueDate().isAfter(now.plusDays(3))) {
            throw new IllegalArgumentException("Task cannot be scheduled more than 3 days in advance");
        }

        if (task.getTags().size() < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }
    }
}
