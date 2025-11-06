package com.nixo.web; // adjust if your servlet package is different

import com.nixo.entity.News;
import com.nixo.entity.User;
import com.nixo.service.NewsService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/NewsServlet")
public class NewsServlet extends HttpServlet {

    @EJB
    private NewsService newsService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "create":
                // Show create form (only for logged-in users; filter should already enforce)
                request.getRequestDispatcher("/WEB-INF/createNews.jsp").forward(request, response);
                break;

            case "edit":
                handleEditGet(request, response);
                break;

            case "delete":
                handleDelete(request, response);
                break;

            case "view":
                handleView(request, response);
                break;

            case "search":
                handleSearch(request, response);
                break;

            case "like":
                handleLike(request, response);
                break;

            case "list":
            default:
                // Redirect to canonical feed endpoint
                response.sendRedirect(request.getContextPath() + "/feed");
                break;
        }
    }

    // POST handles create and update
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Make sure request uses the same action parameter
        String action = request.getParameter("action");
        if (action == null) action = "create";

        switch (action) {
            case "create":
                handleCreatePost(request, response);
                break;

            case "update":
                handleUpdatePost(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/feed");
                break;
        }
    }

    // ---------- Handlers ----------

    private void handleCreatePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Basic server-side validation
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "Title and content are required.");
            request.getRequestDispatcher("/WEB-INF/createNews.jsp").forward(request, response);
            return;
        }

        // Get logged-in user from session (must be non-null; filter should ensure)
        HttpSession session = request.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        News news = new News();
        news.setTitle(title.trim());
        news.setContent(content.trim());
        // set sharer/author - assumes News has setSharer(User)
        news.setSharer(loggedUser);
        news.setStatus("pending");

        // default status pending (admin will approve) OR set to 'approved' in dev
        // news.setStatus("pending");

        newsService.addNews(news);

        // After create -> send to feed
        response.sendRedirect(request.getContextPath() + "/feed");
    }

    private void handleEditGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }

        int id = Integer.parseInt(idStr);
        News n = newsService.findById(id);
        if (n == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }

        // Authorization: only author or admin may edit
        if (!isOwnerOrAdmin(request, n)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to edit this news.");
            return;
        }

        request.setAttribute("id", n.getId());
        request.setAttribute("title", n.getTitle());
        request.setAttribute("content", n.getContent());
        request.getRequestDispatcher("/WEB-INF/editNews.jsp").forward(request, response);
    }

    private void handleUpdatePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }
        int id = Integer.parseInt(idStr);

        News existing = newsService.findById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }

        // Authorization check
        if (!isOwnerOrAdmin(request, existing)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to update this news.");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "Title and content cannot be empty.");
            // keep id/title/content in request so edit page can show them
            request.setAttribute("id", existing.getId());
            request.setAttribute("title", title);
            request.setAttribute("content", content);
            request.getRequestDispatcher("/WEB-INF/editNews.jsp").forward(request, response);
            return;
        }

        existing.setTitle(title.trim());
        existing.setContent(content.trim());
        newsService.updateNews(existing);

        response.sendRedirect(request.getContextPath() + "/feed");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }
        int id = Integer.parseInt(idStr);
        News n = newsService.findById(id);
        if (n == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }

        // Only owner or admin may delete
        if (!isOwnerOrAdmin(request, n)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to delete this news.");
            return;
        }

        newsService.deleteNews(id);
        response.sendRedirect(request.getContextPath() + "/feed");
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }
        int id = Integer.parseInt(idStr);
        News n = newsService.findById(id);
        if (n == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }

        request.setAttribute("news", n);
        request.getRequestDispatcher("/WEB-INF/viewNews.jsp").forward(request, response);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<News> results;
        if (keyword == null || keyword.trim().isEmpty()) {
            results = newsService.getAllNews();
        } else {
            results = newsService.searchByTitle(keyword.trim());
            request.setAttribute("searchKeyword", keyword.trim());
        }
        request.setAttribute("newsList", results);
        request.getRequestDispatcher("/WEB-INF/feed.jsp").forward(request, response);
    }

    private void handleLike(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }
        int id = Integer.parseInt(idStr);

        // If your NewsService supports per-user liking:
        try {
            newsService.likeNewsByUser(id, loggedUser.getUserId());
        } catch (UnsupportedOperationException e) {
            // fallback: simple increment (not user-unique)
            newsService.likeNews(id);
        }

        // Prefer redirect back to feed or to referer
        String referer = request.getHeader("Referer");
        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/feed");
        }
    }

    // ---------- Helper ----------

    private boolean isOwnerOrAdmin(HttpServletRequest req, News news) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        User logged = (User) session.getAttribute("loggedUser");
        if (logged == null) return false;

        // If user is admin (assumes role name in session or role object)
        String roleName = (String) session.getAttribute("role");
        if ("admin".equalsIgnoreCase(roleName)) return true;

        // Compare owner id
        if (news.getSharer() != null) {
            int ownerId = news.getSharer().getUserId(); // adjust getter name if different
            return ownerId == logged.getUserId();
        }

        return false;
    }
}
