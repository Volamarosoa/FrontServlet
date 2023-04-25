package models.employees;
import dao.annotations.Colonne;
import dao.annotations.Table;
import dao.generiqueDAO.GeneriqueDAO;
import etu2068.annotations.Url;
import etu2068.modelView.ModelView;
import java.util.List;




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

    public void setNumero(String numero) {
        this.setNumero(Integer.parseInt(numero));
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

}
