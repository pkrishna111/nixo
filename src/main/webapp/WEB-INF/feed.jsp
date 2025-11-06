<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, java.time.format.DateTimeFormatter" %>
<%@ page import="com.nixo.entity.User, com.nixo.entity.News" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    String roleName = (loggedUser != null && loggedUser.getRole() != null)
            ? loggedUser.getRole().getRoleName().toLowerCase()
            : null;

    // For like-toggled state: controller should set Set<Integer> likedNewsIds
    Set<Integer> likedNewsIds = (Set<Integer>) request.getAttribute("likedNewsIds");
    if (likedNewsIds == null) likedNewsIds = new HashSet<>(); // safe fallback

    // Optional status for receiver’s sharer request (already set in your FeedServlet)
    String sharerReqStatus = (String) request.getAttribute("sharerReqStatus");

    // Date formatter for createdAt
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    String ctx = request.getContextPath();
%>
<html>
<head>
    <title>Nixo Feed</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Page background */
        body {
            background: linear-gradient(135deg, #f7f9fc, #eef2f7);
            min-height: 100vh;
        }

        /* Glass/blur navbar */
        .glass-nav {
            position: sticky;
            top: 0;
            z-index: 1050;
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            background: rgba(255, 255, 255, 0.55);
            border-bottom: 1px solid rgba(0, 0, 0, 0.06);
        }
        .nav-inner {
            max-width: 1100px;
            margin: 0 auto;
            padding: 10px 16px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .brand {
            font-weight: 800;
            letter-spacing: 0.4px;
            text-transform: uppercase;
        }

        /* Dynamic menu (left) */
        .menu-trigger {
            border: 0;
            background: rgba(0,0,0,0.04);
            border-radius: 12px;
            padding: 8px 12px;
        }
        .dropdown-menu {
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.12);
        }

        /* Greeting (right) */
        .greet {
            font-weight: 600;
        }
        .greet.admin { color: #0d6efd; }
        .greet.sharer { color: #198754; }
        .greet.receiver { color: #6c757d; }

        /* Search bar beautify */
        .search-wrap {
            max-width: 900px;
            margin: 20px auto 10px auto;
        }

        /* Card styles */
        .news-card {
            border: 0;
            border-radius: 18px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(16, 24, 40, 0.06);
            background: linear-gradient(180deg, #ffffff 0%, #fbfcff 100%);
            transition: transform .2s ease, box-shadow .2s ease;
        }
        .news-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 16px 40px rgba(16,24,40,0.10);
        }
        .news-meta {
            font-size: 0.9rem;
            color: #6b7280;
        }

        /* Like button states */
        .btn-like {
            border-radius: 999px;
        }
        .btn-like.active {
            background-color: #0d6efd;
            color: #fff;
            border-color: #0d6efd;
        }

        /* Floating Add (+) for sharer/admin */
        .fab-add {
            position: fixed;
            right: 22px;
            bottom: 22px;
            width: 58px;
            height: 58px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            color: #fff;
            background: #0d6efd;
            box-shadow: 0 16px 35px rgba(13,110,253,0.45);
            text-decoration: none;
            transition: transform .15s ease, box-shadow .15s ease;
            z-index: 1049;
        }
        .fab-add:hover {
            transform: translateY(-2px);
            box-shadow: 0 22px 40px rgba(13,110,253,0.55);
            color: #fff;
        }

        /* Toast position */
        .toast-zone {
            position: fixed;
            top: 80px;
            right: 16px;
            z-index: 2000;
        }
    </style>
</head>
<body>

<!-- ✅ Glass/Blur Navbar -->
<nav class="glass-nav">
    <div class="nav-inner">
        <div class="d-flex align-items-center gap-2">
            <button class="menu-trigger btn btn-light btn-sm dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                ☰ Menu
            </button>
            <ul class="dropdown-menu">
                <% if (loggedUser != null) { %>
                    <% if ("admin".equals(roleName)) { %>
                        <li><a class="dropdown-item" href="<%= ctx %>/SharerRequestServlet?action=list">🧾 View Sharer Requests</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="<%= ctx %>/logout">Logout</a></li>
                    <% } else if ("sharer".equals(roleName)) { %>
                        <li><a class="dropdown-item text-danger" href="<%= ctx %>/logout">Logout</a></li>
                    <% } else { %>
                        <% if (sharerReqStatus == null) { %>
                            <li><a class="dropdown-item" href="<%= ctx %>/SharerRequestServlet?action=applyForm">🌟 Become a Sharer</a></li>
                        <% } else if ("rejected".equalsIgnoreCase(sharerReqStatus)) { %>
                            <li><a class="dropdown-item" href="<%= ctx %>/SharerRequestServlet?action=applyForm">❌ Reapply as Sharer</a></li>
                        <% } %>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="<%= ctx %>/logout">Logout</a></li>
                    <% } %>
                <% } else { %>
                    <li><a class="dropdown-item" href="<%= ctx %>/login">Login</a></li>
                    <li><a class="dropdown-item" href="<%= ctx %>/register">Register</a></li>
                <% } %>
            </ul>

            <span class="brand ms-2">NIXO</span>
        </div>

        <div>
            <% if (loggedUser != null) { %>
                <% if ("admin".equals(roleName)) { %>
                    <span class="greet admin">🧑‍💼 Hello Admin, <%= loggedUser.getName() %></span>
                <% } else if ("sharer".equals(roleName)) { %>
                    <span class="greet sharer">📰 Hello Sharer, <%= loggedUser.getName() %></span>
                <% } else { %>
                    <span class="greet receiver">👤 Hello Receiver, <%= loggedUser.getName() %></span>
                <% } %>
            <% } %>
        </div>
    </div>
</nav>

<!-- ✅ Toast Notification -->
<div class="toast-zone p-3">
    <div id="nixoToast" class="toast align-items-center text-bg-dark border-0" role="alert"
         aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <%= (request.getAttribute("toastMessage") != null) ? request.getAttribute("toastMessage") : "" %>
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                    aria-label="Close"></button>
        </div>
    </div>
</div>

<div class="container mt-4" style="max-width: 980px;">

    <!-- Search bar -->
    <form class="input-group mb-4 search-wrap" action="<%= ctx %>/NewsServlet" method="get">
        <input type="hidden" name="action" value="search">
        <input type="text" name="keyword" class="form-control form-control-lg" placeholder="Search news by title..."
               value="<%= request.getAttribute("searchKeyword") != null ? request.getAttribute("searchKeyword") : "" %>">
        <button class="btn btn-primary btn-lg" type="submit">🔍 Search</button>
        <a href="<%= ctx %>/NewsServlet?action=list" class="btn btn-outline-secondary btn-lg">Reset</a>
    </form>

    <%
        List<News> newsList = (List<News>) request.getAttribute("newsList");
        if (newsList != null && !newsList.isEmpty()) {
            for (News n : newsList) {
                boolean userOwns = (loggedUser != null && n.getSharer() != null && n.getSharer().getUserId() == loggedUser.getUserId());
                boolean likedByMe = likedNewsIds.contains(n.getId());
                String authorName = (n.getSharer() != null && n.getSharer().getName() != null) ? n.getSharer().getName() : "Unknown";
                String when = (n.getCreatedAt() != null) ? n.getCreatedAt().format(fmt) : "";
    %>
    <div class="card news-card mb-4">
        <div class="card-body p-4">
            <h3 class="card-title mb-2"><%= n.getTitle() %></h3>

            <div class="news-meta mb-3">
                By <strong><%= authorName %></strong> &middot; <span><%= when %></span>
            </div>

            <p class="card-text fs-6"><%= n.getContent() %></p>

            <div class="mt-3 d-flex justify-content-between align-items-center">
                <div class="d-flex gap-2">
                    <% if (loggedUser != null && ("admin".equals(roleName) || ( "sharer".equals(roleName) && userOwns))) { %>
                        <a href="<%= ctx %>/NewsServlet?action=edit&id=<%= n.getId() %>" class="btn btn-warning btn-sm">✏️ Edit</a>
                        <a href="<%= ctx %>/NewsServlet?action=delete&id=<%= n.getId() %>" class="btn btn-danger btn-sm"
                           onclick="return confirm('Are you sure you want to delete this news?');">🗑️ Delete</a>
                    <% } %>
                </div>

                <div>
                    <% if (loggedUser != null) { %>
                        <a href="<%= ctx %>/NewsServlet?action=like&id=<%= n.getId() %>"
                           class="btn btn-outline-primary btn-like btn-sm <%= likedByMe ? "active" : "" %>">
                            <%= likedByMe ? "💙 Liked" : "🤍 Like" %> (<%= n.getLikes() %>)
                        </a>
                    <% } else { %>
                        <a href="<%= ctx %>/login" class="btn btn-outline-secondary btn-sm disabled">❤️ Like (Login Required)</a>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    <%
            }
        } else {
    %>
    <p class="text-center text-muted">No news found.</p>
    <% } %>
</div>

<%-- Floating Add (+) visible only for sharer/admin --%>
<% if (loggedUser != null && ("admin".equals(roleName) || "sharer".equals(roleName))) { %>
    <a class="fab-add" href="<%= ctx %>/NewsServlet?action=create" title="Add News">+</a>
<% } %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Show toast if message exists
    document.addEventListener('DOMContentLoaded', function () {
        const toastEl = document.getElementById('nixoToast');
        const hasMessage = '<%= (request.getAttribute("toastMessage") != null) ? "true" : "false" %>';
        if (toastEl && hasMessage === 'true') {
            const toast = new bootstrap.Toast(toastEl, { delay: 5000 });
            toast.show();
        }
    });
</script>
</body>
</html>
