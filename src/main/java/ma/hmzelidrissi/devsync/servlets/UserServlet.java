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
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/admin/users/*")
public class UserServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null || currentUser.getRole() != Role.MANAGER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listUsers(request, response);
        } else if (pathInfo.equals("/new")) {
            showUserForm(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            editUser(request, response);
        } else if (pathInfo.startsWith("/delete/")) {
            deleteUser(request, response);
        } else {
            viewUser(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.MANAGER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createUser(request, response);
            } else if ("update".equals(action)) {
                updateUser(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("users", userService.getAllUsers());
        request.getRequestDispatcher("/views/users/list.jsp").forward(request, response);
    }

    private void showUserForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("editUser", new User());
        request.getRequestDispatcher("/views/users/form.jsp").forward(request, response);
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long userId = Long.valueOf(request.getPathInfo().substring("/edit/".length()));
            User user = userService.getUserById(userId);
            if (user != null) {
                request.setAttribute("editUser", user);  // Use "editUser" instead of "user"
                request.getRequestDispatcher("/views/users/form.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long userId = Long.valueOf(request.getPathInfo().substring("/delete/".length()));
            userService.deleteUser(userId);
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        }
    }

    private void viewUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long userId = Long.valueOf(request.getPathInfo().substring(1));
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

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User newUser = new User();
        populateUser(newUser, request);
        userService.createUser(newUser);
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("id"));
        User user = userService.getUserById(userId);
        if (user != null) {
            populateUser(user, request);
            userService.updateUser(user);
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    private void populateUser(User user, HttpServletRequest request) {
        user.setUsername(request.getParameter("username"));
        String password = request.getParameter("password");
        if (password != null && !password.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(hashedPassword);
        }
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setRole(Role.valueOf(request.getParameter("role")));
    }
}