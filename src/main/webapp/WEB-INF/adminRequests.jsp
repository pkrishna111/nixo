<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, com.nixo.entity.SharerRequest" %>
<html>
<head>
    <title>Admin Panel - Sharer Requests | NIXO</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>🛠 Admin Panel - Sharer Requests</h2>
        <div>
            <a href="feed" class="btn btn-secondary btn-sm">🏠 Back to Feed</a>
            <a href="logout" class="btn btn-danger btn-sm">Logout</a>
        </div>
    </div>

    <%
        List<SharerRequest> requests = (List<SharerRequest>) request.getAttribute("requests");
        if (requests == null || requests.isEmpty()) {
    %>
        <div class="alert alert-info text-center">No pending sharer requests found.</div>
    <%
        } else {
    %>

    <table class="table table-bordered table-striped shadow-sm align-middle">
        <thead class="table-dark">
        <tr>
            <th>#</th>
            <th>User Name</th>
            <th>Email</th>
            <th>Reason</th>
            <th>Date</th>
            <th>Status</th>
            <th class="text-center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            int i = 1;
            for (SharerRequest r : requests) {
        %>
        <tr>
            <td><%= i++ %></td>
            <td><%= r.getUser().getName() %></td>
            <td><%= r.getUser().getEmail() %></td>
            <td><%= r.getReason() %></td>
            <td><%= r.getCreatedAt() != null ? r.getCreatedAt() : "N/A" %></td>
            <td>
                <span class="badge bg-warning text-dark">
                    <%= r.getStatus() != null ? r.getStatus() : "Pending" %>
                </span>
            </td>
            <td class="text-center">
                <form action="SharerRequestServlet" method="post" style="display:inline-block">
                    <input type="hidden" name="action" value="approve">
                    <input type="hidden" name="id" value="<%= r.getId() %>">
                    <button type="submit" class="btn btn-success btn-sm"
                            onclick="return confirm('Approve this request?');">
                        ✅ Approve
                    </button>
                </form>

                <form action="SharerRequestServlet" method="post" style="display:inline-block">
                    <input type="hidden" name="action" value="reject">
                    <input type="hidden" name="id" value="<%= r.getId() %>">
                    <button type="submit" class="btn btn-danger btn-sm"
                            onclick="return confirm('Reject this request?');">
                        ❌ Reject
                    </button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <%
        }
    %>
</div>

</body>
</html>
