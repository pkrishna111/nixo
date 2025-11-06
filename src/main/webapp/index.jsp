<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>NIXO - Home</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <h1 class="text-center">Welcome to NIXO 📰</h1>
            <p class="text-center text-muted">A platform to share and read news.</p>

            <div class="text-center">
                <a href="login">Login</a> | 
                <a href="register">Register</a>
            </div>
            <div class="text-center mt-4">
                <a href="feed" class="btn btn-primary">View All News</a>
                <a href="NewsServlet?action=create" class="btn btn-success">Add News</a>
            </div>
        </div>
    </body>
</html>
