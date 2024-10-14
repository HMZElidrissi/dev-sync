package ma.hmzelidrissi.devsync.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.hmzelidrissi.devsync.entities.Role;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TaskService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/overview")
public class ManagerOverviewServlet extends HttpServlet {
    @Inject
    private TaskService taskService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User manager = (User) request.getSession().getAttribute("user");
        if (manager.getRole() != Role.MANAGER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Map<String, Object> overview = taskService.getManagerOverview(manager.getId());
        request.setAttribute("overview", overview);
        request.getRequestDispatcher("/views/manager/dashboard.jsp").forward(request, response);
    }
}
