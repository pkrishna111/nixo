package com.nixo.servlet;

import com.nixo.entity.News;
import com.nixo.entity.User;
import com.nixo.service.NewsService;
import com.nixo.service.SharerRequestService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

    @EJB
    private NewsService newsService;

    @EJB
    private SharerRequestService requestService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser != null && "receiver".equalsIgnoreCase(loggedUser.getRole().getRoleName())) {
            String reqStatus = requestService.getRequestStatusByUser(loggedUser);
            request.setAttribute("sharerReqStatus", reqStatus);
        }

        List<News> allNews = newsService.getAllNews();
        request.setAttribute("newsList", allNews);
        if (loggedUser != null && "receiver".equalsIgnoreCase(loggedUser.getRole().getRoleName())) {
            String reqStatus = requestService.getRequestStatusByUser(loggedUser);
            request.setAttribute("sharerReqStatus", reqStatus);

            // ✅ Show toast message for approved/rejected requests (one-time)
            if ("approved".equalsIgnoreCase(reqStatus)) {
                request.setAttribute("toastMessage", "🎉 Your sharer request was approved! You now have access to share news.");
            } else if ("rejected".equalsIgnoreCase(reqStatus)) {
                request.setAttribute("toastMessage", "❌ Your sharer request was rejected. You can reapply anytime.");
            }
        }
        if (request.getAttribute("toastMessage") != null) {
            session.removeAttribute("toastMessageShown");
        }

        request.getRequestDispatcher("/WEB-INF/feed.jsp").forward(request, response);
    }
}
