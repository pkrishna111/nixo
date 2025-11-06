<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit News</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <h2 class="mb-4">✏️ Edit News</h2>

    <form action="NewsServlet?action=update" method="post">
        <input type="hidden" name="id" value="<%= request.getAttribute("id") %>">
        <div class="mb-3">
            <label class="form-label">Title</label>
            <input type="text" name="title" class="form-control" value="<%= request.getAttribute("title") %>" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Content</label>
            <textarea name="content" class="form-control" rows="5" required><%= request.getAttribute("content") %></textarea>
        </div>
        <button type="submit" class="btn btn-primary">💾 Update</button>
        <a href="NewsServlet?action=list" class="btn btn-secondary">⬅ Back</a>
    </form>
</div>
</body>
</html>
