package com.nixo.servlet;

import com.nixo.entity.SharerRequest;
import com.nixo.entity.User;
import com.nixo.service.SharerRequestService;
import com.nixo.service.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/SharerRequestServlet")
public class SharerRequestServlet extends HttpServlet {

    @EJB
    private SharerRequestService requestService;

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("apply".equals(action)) {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("loggedUser");

            if (user == null) {
                request.setAttribute("error", "You must be logged in to apply.");
            } else if ("sharer".equalsIgnoreCase(user.getRole().getRoleName())) {
                request.setAttribute("error", "You are already a sharer.");
            } else if (requestService.hasPendingRequest(user)) {
                request.setAttribute("error", "You already have a pending request awaiting admin approval.");
            } else {
                String reason = request.getParameter("reason");
                requestService.submitRequest(user, reason);
                request.setAttribute("message", "Your request has been submitted for admin review.");
            }

            request.getRequestDispatcher("/WEB-INF/registerSharer.jsp").forward(request, response);
        } else if ("approve".equals(action) || "reject".equals(action)) {
            HttpSession session = request.getSession(false);
            User admin = (session != null) ? (User) session.getAttribute("loggedUser") : null;

            if (admin == null || !"admin".equalsIgnoreCase(admin.getRole().getRoleName())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only admin can perform this action.");
                return;
            }

            int reqId = Integer.parseInt(request.getParameter("id"));
            String status = "approve".equals(action) ? "approved" : "rejected";

            requestService.updateStatus(reqId, status);

            // If approved → update user role to sharer
            if ("approved".equals(status)) {
                SharerRequest req = requestService.findById(reqId);
                if (req != null) {
                    userService.updateUserRole(req.getUser().getUserId(), "sharer");
                }
            }

            // ✅ Redirect back to servlet list view
            response.sendRedirect(request.getContextPath() + "/SharerRequestServlet?action=list");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
        } else {
            switch (action) {
                case "applyForm":
                    request.getRequestDispatcher("/WEB-INF/registerSharer.jsp").forward(request, response);
                    break;
                case "list":
                    HttpSession session = request.getSession(false);
                    User logged = (session != null) ? (User) session.getAttribute("loggedUser") : null;

                    if (logged != null && "admin".equalsIgnoreCase(logged.getRole().getRoleName())) {
                        request.setAttribute("requests", requestService.getPendingRequests());

                        // ✅ Forward correctly from servlet context
                        getServletContext()
                                .getRequestDispatcher("/WEB-INF/adminRequests.jsp")
                                .forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                    }
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        }
    }

}
