<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Login - Nixo</title>
    </head>
    <body>
        <h2>Login to Nixo</h2>

        <form action="login" method="post">
            <label>Email:</label><br/>
            <input type="email" name="email" required><br/><br/>

            <label>Password:</label><br/>
            <input type="password" name="password" required><br/><br/>

            <input type="submit" value="Login">
        </form>

        <p style="color:red;">
            <%= request.getAttribute("error") == null ? "" : request.getAttribute("error")%>
        </p>

        <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
        <% } %>

        <% if (request.getAttribute("message") != null) {%>
            <p style="color:green;"><%= request.getAttribute("message")%></p>
        <% }%>


        <p>New user? <a href="register">Register here</a>.</p>
    </body>
</html>
