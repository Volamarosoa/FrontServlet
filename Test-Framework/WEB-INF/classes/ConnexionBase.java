package connexionBase;
import java.sql.*;

public class ConnexionBase {
    String utilisateur = "postgres";
    String password = "fanatenana";
    Connection connection = null;
    int validation = 1;

    public ConnexionBase(){
        try{ 
            this.connexion(); 
            this.getConnection().setAutoCommit(false);
        }
	catch(Exception io){ }
    }

    public String getUtilisateur(){
	return this.utilisateur;
    }

    public void setUtilisateur(String n){
	this.utilisateur = n;
    }

    public String getPassword(){
	return this.password;
    }

    public void setPassword(String p){
	this.password = p;
    }

    public Connection getConnection(){
	return this.connection;
    }

    public void setConnection(Connection t){
	this.connection = t;	
    }

    public void connexion() {	// connexion du java avec oracle
        if(this.getConnection()==null){
                try{

                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/entreprise",this.getUtilisateur(),this.getPassword());
                    this.setConnection(conn);
                }
                catch(Exception io){
                    io.printStackTrace();
                }
        }
    }

    public int getValidation(){
		return this.validation;
	}

	public void setValidation(int n){
		this.validation = n;
	}

    public void testConnexion(ConnexionBase con){
		int a = 1;
		if(con==null){
			a = 0;
		}
		this.setValidation(a);
	}

    public static ConnexionBase verifierConnexion(ConnexionBase con){
        ConnexionBase c = con;
		if(c==null){
			c = new ConnexionBase();
            c.setValidation(0);
		} 
		return c;
	}

	public static ConnexionBase fermetureConnexion(ConnexionBase con) throws Exception{
        ConnexionBase c = con;
		if(c.getValidation()==0){
			c.getConnection().commit();
			c.getConnection().close();
			c = null;
		}
		
		return c;
	} 
}
