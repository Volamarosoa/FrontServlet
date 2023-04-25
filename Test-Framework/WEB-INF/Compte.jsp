<%@page import="models.compte.Compte, java.util.List" %>
<%
    List<Compte> liste = (List<Compte>)request.getAttribute("liste");
    out.println(liste.size());
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Compte</title>
</head>
<body>
    <h4>Listes des compte</h4>
    <table border="1">
        <th>Compte</th>
        <th>Intitule</th>
        <% for(int i=0; i<liste.size(); i++) { %>
        <tr>
            <td><%= liste.get(i).getNumero() %></td>
            <td><%= liste.get(i).getIntitule() %></td>
        </tr>
        <% } %>
        <% for(int i=0; i<listeB.size(); i++) { %>
            <tr>
                <td><%= listeB.get(i).getNumero() %></td>
                <td><%= listeB.get(i).getIntitule() %></td>
            </tr>
            <% } %>
    </table>
</body>
</html>