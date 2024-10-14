package ma.hmzelidrissi.devsync.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.UserService;

import java.io.IOException;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.authenticateUser(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            String dashboard = (user.getRole() == Role.MANAGER) ? "/admin/users" : "/user/tasks";
            response.sendRedirect(request.getContextPath() + dashboard);
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }
}
