<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Register as News Sharer</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    </head>
    <body>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Apply to become a News Sharer</h2>
            <div>
                <a href="feed" class="btn btn-secondary btn-sm">🏠 Back to Feed</a>
                <a href="logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>

        <% String msg = (String) request.getAttribute("message");
            String err = (String) request.getAttribute("error");
            if (msg != null) {%><p style="color:green;"><%=msg%></p><% }
                if (err != null) {%><p style="color:red;"><%=err%></p><% }%>

        <form action="SharerRequestServlet" method="post">
            <input type="hidden" name="action" value="apply">
            <label>Why do you want to be a sharer?</label><br>
            <textarea name="reason" rows="4" cols="50" required></textarea><br><br>
            <button type="submit">Submit Request</button>
        </form>

    </body>
</html>
