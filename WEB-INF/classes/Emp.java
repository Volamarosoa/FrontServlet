package models.employees;
import dao.annotations.Colonne;
import dao.annotations.Table;
import dao.generiqueDAO.GeneriqueDAO;

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
}
