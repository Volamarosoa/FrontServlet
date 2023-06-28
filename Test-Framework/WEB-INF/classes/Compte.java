package models.compte;
import dao.annotations.Colonne;
import dao.annotations.Table;
import dao.generiqueDAO.GeneriqueDAO;

import etu2068.modelView.ModelView;
import etu2068.annotations.Url;
import etu2068.annotations.Argument;
import etu2068.annotations.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
@Table
public class Compte extends GeneriqueDAO{
    @Colonne
    int id = -1;
    @Colonne
    int idEntreprise = -1;
    @Colonne
    int numero = -1;
    @Colonne
    String intitule;
    @Colonne
    int exist = -1;

    public Compte() {}
        
    public Compte(String numero, String intitule) {
        this.setNumero(numero);
        this.setIntitule(intitule);
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        this.setId(Integer.parseInt(id));
    }
    
    public int getIdEntreprise() {
        return this.idEntreprise;
    }
    
    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }
    
    public void setIdEntreprise(String idEntreprise) {
        this.setIdEntreprise(Integer.parseInt(idEntreprise));
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


    public String getIntitule() {
        return this.intitule;
    }
    
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
    

    public int getExist() {
        return this.exist;
    }

    public void setExist(int exist) {
        this.exist = exist;
    }

    public void setExist(String exist) {
        this.setExist(exist);
    }

    public void reinitialiser() {
        this.id = -1;
        this.idEntreprise = -1;
        this.numero = -1;
        this.intitule = "";
        this.exist = -1;
    }

    @Url(name = "/page")
    public ModelView modelView(@Argument(name = "a") int a,@Argument(name = "b") int b) throws Exception {
        ModelView view = new ModelView("Compte.jsp");
        List<Compte> liste = (List<Compte>)new Compte().list(null);
        view.addItem("liste", liste);
        view.addItem("addition", (a+b));
        return view;
    }
}
