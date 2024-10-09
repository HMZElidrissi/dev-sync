package ma.hmzelidrissi.devsync.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TaskService;
import ma.hmzelidrissi.devsync.services.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {

    @Inject
    private TaskService taskService;

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listTasks(request, response);
        } else if (pathInfo.equals("/new")) {
            showTaskForm(request, response);
        } else if (pathInfo.equals("/kanban")) {
            showKanbanView(request, response);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                viewTask(request, response, Long.parseLong(splits[1]));
            } else if (splits.length == 3 && splits[1].equals("edit")) {
                showEditForm(request, response, Long.parseLong(splits[2]));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            createTask(request, response);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 3 && splits[2].equals("status")) {
                updateTaskStatus(request, response, Long.parseLong(splits[1]));
            } else if (splits.length == 3 && splits[2].equals("replace")) {
                replaceTask(request, response, Long.parseLong(splits[1]));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            Long taskId = Long.parseLong(pathInfo.substring(1));
            deleteTask(request, response, taskId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks = taskService.getAllTasks();
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/views/tasks/list.jsp").forward(request, response);
    }

    private void showKanbanView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks = taskService.getAllTasks();
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/views/tasks/kanban.jsp").forward(request, response);
    }

    private void showTaskForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/views/tasks/form.jsp").forward(request, response);
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

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, Long id) throws ServletException, IOException {
        Task task = taskService.getTaskById(id);
        List<User> users = userService.getAllUsers();
        if (task != null) {
            request.setAttribute("task", task);
            request.setAttribute("users", users);
            request.getRequestDispatcher("/views/tasks/form.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createTask(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Task task = new Task();
        task.setTitle(request.getParameter("title"));
        task.setDescription(request.getParameter("description"));
        task.setDueDate(LocalDateTime.parse(request.getParameter("dueDate")));

        String[] tags = request.getParameter("tags").split(",");
        task.setTags(new HashSet<>(Arrays.asList(tags)));

        Long assignedUserId = Long.parseLong(request.getParameter("assignedTo"));

        try {
            taskService.createTask(task, assignedUserId);
            response.sendRedirect(request.getContextPath() + "/tasks");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/tasks/form.jsp").forward(request, response);
        }
    }

    private void updateTaskStatus(HttpServletRequest request, HttpServletResponse response, Long id) throws IOException {
        boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
        try {
            if (completed) {
                taskService.markTaskAsCompleted(id);
            } else {
                // Implement reopening a task if needed
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void replaceTask(HttpServletRequest request, HttpServletResponse response, Long id) throws IOException, ServletException {
        Task newTask = new Task();
        newTask.setTitle(request.getParameter("title"));
        newTask.setDescription(request.getParameter("description"));
        newTask.setDueDate(LocalDateTime.parse(request.getParameter("dueDate")));

        String[] tags = request.getParameter("tags").split(",");
        newTask.setTags(new HashSet<>(Arrays.asList(tags)));

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        try {
            taskService.replaceTask(id, currentUser, newTask);
            response.sendRedirect(request.getContextPath() + "/tasks");
        } catch (IllegalArgumentException | IllegalStateException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/tasks/form.jsp").forward(request, response);
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response, Long id) throws IOException {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        try {
            taskService.deleteTask(id, currentUser);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}