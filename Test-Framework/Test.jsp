<%
    out.println(request.getAttribute("nom"));
    String nom = (String)session.getAttribute("Rota");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test - Page</title>
</head>
<body>
    <h3>PAGE DE TEST, Bienvenu</h3>
    <p>Session: <%= nom %></p>
</body>
</html>