<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Register - Nixo</title>
    </head>
    <body>
        <h2>Register as a New User</h2>

        <form action="register" method="post">
            <label>Name:</label><br/>
            <input type="text" name="name" required><br/><br/>

            <label>Email:</label><br/>
            <input type="email" name="email" required><br/><br/>

            <label>Password:</label><br/>
            <input type="password" name="password" required><br/><br/>

            <input type="submit" value="Register">
        </form>

        <p style="color:green;">
            <%= request.getAttribute("message") == null ? "" : request.getAttribute("message")%>
        </p>

        <% if (request.getAttribute("error") != null) {%>
            <p style="color:red;"><%= request.getAttribute("error")%></p>
        <% } %>

        <% if (request.getAttribute("message") != null) {%>
            <p style="color:green;"><%= request.getAttribute("message")%></p>
        <% }%>


        <p>Already have an account? <a href="login">Login here</a>.</p>
    </body>
</html>
