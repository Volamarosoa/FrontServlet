<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire</title>
</head>
<body>
    <form action="Emp/enregistrer" method="POST"  enctype="multipart/form-data">
        <h3>Ajout d'un Nouveau Employe</h3>
        <p>Nom: <input type="text" name="nom"></p>
        <p>Prenom: <input type="text" name="prenom"></p>
        <p>Numero: <input type="number" name="numero"></p>
        <input type="file" name="fichier" />
        <input type="submit" value="Valider">
    </form>
</body>
</html>