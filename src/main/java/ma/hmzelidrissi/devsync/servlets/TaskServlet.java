package ma.hmzelidrissi.devsync.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TaskService;
import ma.hmzelidrissi.devsync.services.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@WebServlet({"/admin/tasks/*", "/user/tasks/*"})
public class TaskServlet extends HttpServlet {

    @Inject
    private TaskService taskService;

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/admin") && currentUser.getRole() != Role.MANAGER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            listTasks(request, response, currentUser);
        } else if (pathInfo.equals("/new")) {
            showTaskForm(request, response, currentUser);
        } else if (pathInfo.startsWith("/view/")) {
            viewTask(request, response, getLongParameter(pathInfo, "/view/"));
        } else if (pathInfo.startsWith("/edit/")) {
            showEditForm(request, response, getLongParameter(pathInfo, "/edit/"), currentUser);
        } else if (pathInfo.startsWith("/replace/")) {
            showReplaceForm(request, response, getLongParameter(pathInfo, "/replace/"), currentUser);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/admin") && currentUser.getRole() != Role.MANAGER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            createTask(request, response, currentUser);
        } else if (pathInfo.startsWith("/status/")) {
            updateTaskStatus(request, response, getLongParameter(pathInfo, "/status/"));
        } else if (pathInfo.startsWith("/replace/")) {
            createTaskChangeRequest(request, response, getLongParameter(pathInfo, "/replace/"), currentUser);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            deleteTask(request, response, getLongParameter(pathInfo, "/"), currentUser);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        List<Task> tasks;
        String viewPath;

        if (currentUser.getRole() == Role.MANAGER) {
            tasks = taskService.getAllTasks();
            viewPath = "/views/tasks/list_manager.jsp";
        } else {
            tasks = taskService.getTasksForUser(currentUser.getId());
            viewPath = "/views/tasks/list_user.jsp";
        }

        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private void showTaskForm(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        if (currentUser.getRole() == Role.MANAGER) {
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
        }
        String viewPath = currentUser.getRole() == Role.MANAGER ? "/views/tasks/form_manager.jsp" : "/views/tasks/form_user.jsp";
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private void viewTask(HttpServletRequest request, HttpServletResponse response, Long id) throws ServletException, IOException {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            request.setAttribute("task", task);
            request.getRequestDispatcher("/views/tasks/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, Long id, User currentUser) throws ServletException, IOException {
        Task task = taskService.getTaskById(id);
        if (task != null && (currentUser.getRole() == Role.MANAGER || task.getCreatedBy().getId().equals(currentUser.getId()))) {
            request.setAttribute("task", task);
            if (currentUser.getRole() == Role.MANAGER) {
                List<User> users = userService.getAllUsers();
                request.setAttribute("users", users);
            }
            String viewPath = currentUser.getRole() == Role.MANAGER ? "/views/tasks/form_manager.jsp" : "/views/tasks/form_user.jsp";
            request.getRequestDispatcher(viewPath).forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void showReplaceForm(HttpServletRequest request, HttpServletResponse response, Long id, User currentUser) throws ServletException, IOException {
        Task task = taskService.getTaskById(id);
        if (task != null && task.getAssignedTo().getId().equals(currentUser.getId()) && !task.getCreatedBy().getId().equals(currentUser.getId())) {
            request.setAttribute("task", task);
            request.getRequestDispatcher("/views/tasks/user_replace_form.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void createTask(HttpServletRequest request, HttpServletResponse response, User currentUser) throws IOException, ServletException {
        try {
            Task task = new Task();
            task.setTitle(request.getParameter("title"));
            task.setDescription(request.getParameter("description"));

            String dueDateStr = request.getParameter("dueDate");
            if (dueDateStr == null || dueDateStr.isEmpty()) {
                throw new IllegalArgumentException("Due date is required");
            }
            LocalDate dueDate = LocalDate.parse(dueDateStr);
            task.setDueDate(dueDate);

            String tagsStr = request.getParameter("tags");
            if (tagsStr == null || tagsStr.isEmpty()) {
                throw new IllegalArgumentException("Tags are required");
            }
            String[] tags = tagsStr.split(",");
            task.setTags(new HashSet<>(Arrays.asList(tags)));

            Long assignedUserId = currentUser.getRole() == Role.MANAGER ?
                    Long.parseLong(request.getParameter("assignedTo")) :
                    currentUser.getId();

            task.setCreatedBy(currentUser);
            taskService.createTask(task, assignedUserId);

            response.sendRedirect(request.getContextPath() + (currentUser.getRole() == Role.MANAGER ? "/admin/tasks" : "/user/tasks"));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating task: " + e.getMessage());
            String viewPath = currentUser.getRole() == Role.MANAGER ? "/views/tasks/form_manager.jsp" : "/views/tasks/form_user.jsp";
            request.getRequestDispatcher(viewPath).forward(request, response);
        }
    }

    private void updateTaskStatus(HttpServletRequest request, HttpServletResponse response, Long id) throws IOException {
        boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
        try {
            if (completed) {
                taskService.markTaskAsCompleted(id);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void createTaskChangeRequest(HttpServletRequest request, HttpServletResponse response, Long taskId, User currentUser) throws IOException, ServletException {
        Task newTaskDetails = new Task();
        newTaskDetails.setTitle(request.getParameter("title"));
        newTaskDetails.setDescription(request.getParameter("description"));
        newTaskDetails.setDueDate(LocalDate.parse(request.getParameter("dueDate")));

        String[] tags = request.getParameter("tags").split(",");
        newTaskDetails.setTags(new HashSet<>(Arrays.asList(tags)));

        try {
            validateTaskCreation(currentUser, currentUser.getId(), newTaskDetails.getDueDate(), tags);
            taskService.createTaskChangeRequest(taskId, currentUser, newTaskDetails);
            response.sendRedirect(request.getContextPath() + "/user/tasks");
        } catch (IllegalArgumentException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/tasks/user_replace_form.jsp").forward(request, response);
        }
    }

    private void replaceTask(HttpServletRequest request, HttpServletResponse response, Long id, User currentUser) throws IOException, ServletException {
        Task newTask = new Task();
        newTask.setTitle(request.getParameter("title"));
        newTask.setDescription(request.getParameter("description"));
        newTask.setDueDate(LocalDate.parse(request.getParameter("dueDate")));

        String[] tags = request.getParameter("tags").split(",");
        newTask.setTags(new HashSet<>(Arrays.asList(tags)));

        try {
            validateTaskCreation(currentUser, currentUser.getId(), newTask.getDueDate(), tags);
            taskService.replaceTask(id, currentUser, newTask);
            response.sendRedirect(request.getContextPath() + "/user/tasks");
        } catch (IllegalArgumentException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/tasks/user_replace_form.jsp").forward(request, response);
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response, Long id, User currentUser) throws IOException {
        try {
            taskService.deleteTask(id, currentUser);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Task deleted successfully");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the task");
        }
    }

    private void validateTaskCreation(User currentUser, Long assignedUserId, LocalDate dueDate, String[] tags) {
        if (!currentUser.getId().equals(assignedUserId) && currentUser.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("You can only assign tasks to yourself");
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Task cannot be created in the past");
        }
        if (dueDate.isAfter(LocalDate.now().plusDays(3))) {
            throw new IllegalArgumentException("Task cannot be scheduled more than 3 days in advance");
        }
        if (tags.length < 2) {
            throw new IllegalArgumentException("Task must have at least 2 tags");
        }
    }

    private Long getLongParameter(String pathInfo, String prefix) {
        return Long.parseLong(pathInfo.substring(prefix.length()));
    }
}
