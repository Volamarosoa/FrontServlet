package main;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import connexionBase.ConnexionBase;
import models.compte.Compte;
import models.employees.Emp;
import etu2068.modelView.ModelView;

public class Main {
    public static void main(String[] args) {
        try{
            ConnexionBase con = new ConnexionBase();
            System.out.println(con.getConnection());
            Emp emp = new Emp();
            List<Emp> liste1 = (List<Emp>)emp.list(null);
            System.out.println( "Liste = " + liste1.size());
            emp.save();
        }
        catch(Exception io) {
            System.out.println(io.getMessage());
        }
    }
}