package ma.hmzelidrissi.devsync.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TaskDAO;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.TaskChangeRequest;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TaskService;
import ma.hmzelidrissi.devsync.services.TokenService;
import ma.hmzelidrissi.devsync.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Task task = taskDAO.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }

        try {
            if (user.getRole() != Role.MANAGER) {
                if (!task.getCreatedBy().getId().equals(user.getId()) && !task.getAssignedTo().getId().equals(user.getId())) {
                    throw new IllegalStateException("You don't have permission to delete this task");
                }
                if (!task.getCreatedBy().getId().equals(user.getId())) {
                    try {
                        tokenService.useDeleteToken(user);
                    } catch (RuntimeException e) {
                        throw new IllegalStateException("Unable to use delete token: " + e.getMessage());
                    }
                }
            }
            taskDAO.delete(taskId);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while deleting the task", e);
        }
    }

    @Override
    public List<Task> getTasksForUser(Long id) {
        return taskDAO.findByUserId(id);
    }

    @Override
    public List<Task> getOverdueTasks(LocalDate today) {
        return taskDAO.findOverdueTasks(today);
    }

    @Override
    public void updateTask(Task task) {
        taskDAO.update(task);
    }

    @Override
    public void replaceTaskByManager(Long oldTaskId, Task newTask, Long newAssigneeId, User manager) {
        if (manager.getRole() != Role.MANAGER) {
            throw new IllegalStateException("Only managers can use this feature");
        }

        Task oldTask = taskDAO.findById(oldTaskId);
        if (oldTask == null) {
            throw new IllegalArgumentException("Task not found");
        }

        User newAssignee = userService.getUserById(newAssigneeId);
        if (newAssignee == null) {
            throw new IllegalArgumentException("New assignee not found");
        }

        if (oldTask.getAssignedTo().getId().equals(newAssigneeId)) {
            throw new IllegalArgumentException("New assignee must be different from the current assignee");
        }

        newTask.setAssignedTo(newAssignee);
        newTask.setCreatedBy(manager);
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setCompleted(false);

        validateTask(newTask);

        taskDAO.delete(oldTaskId);
        Task createdTask = taskDAO.create(newTask);

        createdTask.setLockedByManager(true);
        taskDAO.update(createdTask);
    }

    @Override
    public Map<String, Object> getManagerOverview(Long managerId) {
        User manager = userService.getUserById(managerId);
        if (manager == null || manager.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Invalid manager ID");
        }

        List<User> employees = userService.getAllUsers().stream()
                .filter(user -> user.getRole() == Role.USER)
                .collect(Collectors.toList());
        Map<String, Object> overview = new HashMap<>();

        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.minusDays(7);
        //        LocalDate monthStart = now.withDayOfMonth(1);
        //        LocalDate yearStart = now.withDayOfYear(1);

        for (User employee : employees) {
            Map<String, Object> employeeStats = new HashMap<>();

            List<Task> allTasks = taskDAO.findByAssignedUser(employee.getId());

            employeeStats.put("numberOfTasks", allTasks.size());
            employeeStats.put("tasksCompletedThisWeek", calculateCompletionPercentage(allTasks, weekStart, now));

            int tokensUsed = tokenService.getTokensUsed(employee.getId());
            employeeStats.put("tokensUsed", tokensUsed);

            overview.put(employee.getUsername(), employeeStats);
        }

        return overview;
    }

    @Override
    public void createTaskChangeRequest(Long taskId, User currentUser, Task newTaskDetails) {
        Task task = taskDAO.findById(taskId);
        TaskChangeRequest request = new TaskChangeRequest();
        request.setTask(task);
        request.setRequestor(currentUser);
        request.setAnswered(false);
        request.setRequestTime(LocalDateTime.now());
        taskDAO.createTaskChangeRequest(request);
    }

    private long calculateCompletionPercentage(List<Task> tasks, LocalDate start, LocalDate end) {
        return tasks.stream()
                .filter(task -> !task.getDueDate().isBefore(start) && !task.getDueDate().isAfter(end) && task.isCompleted())
                .count();
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
