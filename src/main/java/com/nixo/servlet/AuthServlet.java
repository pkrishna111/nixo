package com.nixo.servlet;

import com.nixo.entity.Role;
import com.nixo.entity.User;
import com.nixo.service.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/register", "/login", "/logout"})
public class AuthServlet extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/register":
                request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
                break;
            case "/login":
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                break;
            case "/logout":
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect("login");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/register".equals(path)) {
            registerUser(request, response);
        } else if ("/login".equals(path)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || email == null || password == null
                || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
            return;
        }

        // check if email already exists
        User existingUser = userService.findByEmail(email);
        if (existingUser != null) {
            request.setAttribute("error", "Email already registered. Please login instead.");
            request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
            return;
        }

        Role receiverRole = userService.findRoleByName("receiver");
        if (receiverRole == null) {
            // fallback - construct a Role object with name; UserService.registerUser will try to resolve
            receiverRole = new Role();
            receiverRole.setRoleName("receiver");
        }

        User newUser = new User(name, email, password, receiverRole);
        userService.registerUser(newUser);

        request.setAttribute("message", "Registration successful! Please log in.");
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.login(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            session.setAttribute("role", user.getRole().getRoleName());

            // ✅ Correct redirect with context path
            response.sendRedirect(request.getContextPath() + "/feed");
        } else {
            request.setAttribute("error", "Invalid credentials or inactive account.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

}
