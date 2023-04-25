<%@page import="models.employees.Emp, java.util.List" %>
<%
    List<Emp> liste = (List<Emp>)request.getAttribute("listeEmp");
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
    <h4>Listes des employes</h4>
    <table border="1">
        <th>Nom</th>
        <th>Prenom</th>
        <th>Numero</th>
        <% for(int i=0; i<liste.size(); i++) { %>
        <tr>
            <td><%= liste.get(i).getNom() %></td>
            <td><%= liste.get(i).getPrenom() %></td>
            <td><%= liste.get(i).getNumero() %></td>
        </tr>
        <% } %>
    </table>
</body>
</html>