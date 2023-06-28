package models.employees;
import dao.annotations.Colonne;
import dao.annotations.Table;
import dao.generiqueDAO.GeneriqueDAO;

import etu2068.annotations.Url;
import etu2068.annotations.Singleton;
import etu2068.modelView.ModelView;
import etu2068.fileUpload.FileUpload;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;


@Singleton
@Table
public class Emp extends GeneriqueDAO{
    @Colonne
    int id = -1;
    @Colonne
    String nom;
    @Colonne
    String prenom;
    @Colonne
    int numero = -1;
    FileUpload fichier;
    
    
    public Emp() {}
    
    public Emp(String nom, String prenom, int numero) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setNumero(numero);
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return this.nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return this.prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public int getNumero() {
        return this.numero;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public void setNumero(String numero) {
        this.setNumero(Integer.parseInt(numero));
    }

    public FileUpload getFichier() {
        return fichier;
    }

    public byte[] readFileToBytes(String filePath) {
        byte[] bytes = null;
        try {
            Path path = Paths.get(filePath);
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    public void setFichier(byte[] fichier) {
        this.fichier = new FileUpload(fichier);
    }

    public void reinitialiser() throws Exception {
        this.id = -1;
        this.nom = "";
        this.prenom = "";
        this.numero = -1;
        this.fichier = null;
    }

    @Url(name = "/save")
    public ModelView save() throws Exception {
        this.insert(null);
        ModelView view = new ModelView("Employe.jsp");
        List<Emp> liste1 = (List<Emp>)new Emp().list(null);
        System.out.println("taille = " + liste1.size());
        view.addItem("listeEmp", liste1);
        return view;
    }

    @Url(name = "/enregistrer")
    public ModelView enregistrer() throws Exception {
        String str = new String(this.getFichier().getBytes(), StandardCharsets.UTF_8);
        String[] lignes = str.split("\n");
        ModelView view = new ModelView("LireFichier.jsp");
        List<Emp> liste1 = (List<Emp>)new Emp().list(null);
        view.addItem("fichier", lignes);

        return view;
    }

    @Url(name = "/singleton")
    public ModelView singleton() throws Exception {
        ModelView view = new ModelView("Test.jsp");
        view.addItem("id", this.numero);
        view.addItem("nom", this.nom);
        view.addItem("prenom", this.prenom);
        System.out.println("numero = "+ this.numero);
        System.out.println("nom = "+ this.nom);
        System.out.println("prenom = "+ this.prenom);
        return view;
    }

}
