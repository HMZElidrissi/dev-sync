package ma.hmzelidrissi.devsync.servlets;

import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            request.setAttribute("users", userService.getAllUsers());
            request.getRequestDispatcher("/views/users/list.jsp").forward(request, response);
        } else if (pathInfo.equals("/new")) {
            request.getRequestDispatcher("/views/users/form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            try {
                Long userId = Long.valueOf(pathInfo.substring("/edit/".length()));
                User user = userService.getUserById(userId);
                if (user != null) {
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/views/users/form.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            try {
                Long userId = Long.valueOf(pathInfo.substring("/delete/".length()));
                userService.deleteUser(userId);
                response.sendRedirect(request.getContextPath() + "/users");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        } else {
            try {
                Long userId = Long.valueOf(pathInfo.substring(1));
                User user = userService.getUserById(userId);
                if (user != null) {
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/views/users/view.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                User newUser = new User();
                populateUser(newUser, request);
                userService.createUser(newUser);
            } else if ("update".equals(action)) {
                Long userId = Long.valueOf(request.getParameter("id"));
                User user = userService.getUserById(userId);
                if (user != null) {
                    populateUser(user, request);
                    userService.updateUser(user);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    private void populateUser(User user, HttpServletRequest request) {
        user.setUsername(request.getParameter("username"));
        String password = request.getParameter("password");
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setRole(Role.valueOf(request.getParameter("role")));
    }
}