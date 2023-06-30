package models.employees;
import dao.annotations.Colonne;
import dao.annotations.Table;
import dao.generiqueDAO.GeneriqueDAO;

import etu2068.annotations.Url;
import etu2068.annotations.Singleton;
import etu2068.modelView.ModelView;
import etu2068.fileUpload.FileUpload;
import etu2068.annotations.Argument;
import etu2068.annotations.Auth;
import etu2068.annotations.Session;

import java.util.List;
import java.util.HashMap;
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
    HashMap<String, Object> session;

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

    public HashMap<String, Object> getSession() {
        return this.session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
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
        view.addSession("Rota", "Rota kelyy anh!!");
        System.out.println("numero = "+ this.numero);
        System.out.println("nom = "+ this.nom);
        System.out.println("prenom = "+ this.prenom);
        return view;
    }

    @Url(name = "/login")
    public ModelView login(@Argument(name = "e_mail") String e_mail, @Argument(name = "pwd") String pwd) throws Exception {
        ModelView view = new ModelView("Login.jsp");
        view.addItem("e-mail", e_mail);
        view.addItem("pwd", pwd);
        if(e_mail.equals("razafimanatsoarota@gmail.com") && pwd.equals("rota")) {
            view.addSession("isConnected", true);
            view.addSession("profil", "user");
            view.setView("Acceuil.jsp");
        }
        else if(e_mail.equals("admin@gmail.com") && pwd.equals("admin")) {
            view.addSession("isConnected", true);
            view.addSession("profil", "admin");
            view.setView("Acceuil.jsp");
        }
        else {
            view.addSession("isConnected", false);
            System.out.println("Erreurrrr");
        }
        System.out.println("e-mail = "+ e_mail);
        System.out.println("pwd = "+ pwd);
        System.out.println("----------------------------------------------------------");
        return view;
    }

    @Auth(profil = "admin")
    @Url(name = "/test_auth")
    public ModelView test_auth() throws Exception {
        ModelView view = new ModelView("Test.jsp");
        view.addItem("nom", "Rota");
        view.addSession("Rota", "ETU-2068");
        view.addSession("Layah", "ETU-1972");
        view.addSession("Haingo", "ETU-2069");
        return view;
    }

    @Session(sessions = {"isConnected", "profil", "too"})
    @Url(name = "/listeSession")
    public ModelView listeSession() throws Exception {
        ModelView view = new ModelView("TestSession.jsp");
        for (String key : this.getSession().keySet()) {
            Object value = this.getSession().get(key);
            view.addItem(key, value);
            System.out.println(key + " : " + value);
        }
        return view;
    }

    @Url(name = "/test-json")
    public ModelView test_json() throws Exception {
        ModelView view = new ModelView("TestSession.jsp");
        view.setJson(true);
        view.addItem("Rota", "ETU-2068");
        view.addItem("Layah", "ETU-1972");
        view.addItem("Haingo", "ETU-2069");
        return view;
    }

}
