<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Add News - NIXO</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <h2>Add News</h2>
  <form action="NewsServlet" method="post">
    <div class="mb-3">
      <label>Title</label>
      <input type="text" name="title" class="form-control" required>
    </div>
    <div class="mb-3">
      <label>Content</label>
      <textarea name="content" class="form-control" rows="4" required></textarea>
    </div>
    <button class="btn btn-success" type="submit">Save</button>
  </form>
</div>
</body>
</html>
