package ma.hmzelidrissi.devsync.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.entities.Role;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/login";
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isLoginPage = httpRequest.getRequestURI().endsWith("login");

        if (isLoggedIn && (isLoginRequest || isLoginPage)) {
            User user = (User) session.getAttribute("user");
            String dashboard = (user.getRole() == Role.MANAGER) ? "/admin/users" : "/user/tasks";
            httpResponse.sendRedirect(httpRequest.getContextPath() + dashboard);
        } else if (isLoggedIn) {
            User user = (User) session.getAttribute("user");
            if (isAuthorized(user, httpRequest.getRequestURI())) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/unauthorized.jsp");
            }
        } else if (isLoginRequest || isLoginPage) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }

    private boolean isAuthorized(User user, String requestURI) {
        if (user.getRole() == Role.MANAGER) {
            return true;
        } else {
            return requestURI.contains("/user/tasks") || isSharedResource(requestURI);
        }
    }

    private boolean isSharedResource(String requestURI) {
        return requestURI.contains("/resources/") || requestURI.endsWith("/logout");
    }
}