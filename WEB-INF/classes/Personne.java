package models.personne;
import dao.annotations.Colonne;
import dao.annotations.Table;
import dao.generiqueDAO.GeneriqueDAO;

@Table()
public class Personne extends GeneriqueDAO{
    @Colonne
    int id = -1;
    @Colonne
    String nom;
    
    public Personne() {}
    
    public Personne(String nom) {
        this.setNom(nom);
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
    
}
