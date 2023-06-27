<%@page import="models.employees.Emp, java.util.List" %>
<%
    String[] lines = (String[])request.getAttribute("fichier");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fichier</title>
</head>
<body>
    <h5>Dans le fichier:</h5>
    <% for (String line : lines) { %>
        <p><%= line %></p>
    <% } %>
</body>
</html>