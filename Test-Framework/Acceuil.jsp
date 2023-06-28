<%
    out.println(request.getAttribute("e-mail"));
    out.println(request.getAttribute("pwd"));
    String profil = (String)session.getAttribute("profil");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil</title>
</head>
<body>
    <h3>Welcome</h3>
    <p>Profil: <%= profil %></p>
</body>
</html>