package main;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import etu2068.annotations.Argument;


import connexionBase.ConnexionBase;
import models.compte.Compte;
import models.employees.Emp;
import etu2068.modelView.ModelView;

public class Main {
    public static void main(String[] args) {
        try{
            Compte comp = new Compte();
            System.out.println("Boumm");
            Method[] methods = comp.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().equals("modelView")) {
                    Parameter[] parameters = method.getParameters();
                    System.out.println(parameters.length);
                    for (Parameter parameter : parameters) {
                        System.out.println(parameter.getAnnotation(Argument.class).name() + " Type : " + parameter.getType().getName());
                    }
                }
            }
        }
        catch(Exception io) {
            System.out.println(io.getMessage());
            io.printStackTrace();
        }
    }
}