package etu2068.framework.servlet;
import javax.servlet.*;
import javax.servlet.http.*;

import etu2068.annotations.Url;
import etu2068.modelView.ModelView;

import java.io.*;
import java.lang.Thread;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import etu2068.framework.Mapping;
import java.lang.ClassLoader;
import java.util.Enumeration;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;


public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> mappingUrls;
    Vector<Class<?>> listeClasse;

    public void init() {
        try{
            this.setMappingUrls(new HashMap<String,Mapping>());
            ClassLoader classLoader = new Thread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");
            this.listeClasse = new Vector<Class<?>>();

        ///maka ny nom an'ilay package misy an'ilay models rehetra, alaina avy eo amin'ny web.xml    
            String nomPackage = getServletContext().getInitParameter("NomduPackage");

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File dir = new File(url.toURI());
                for (File file : dir.listFiles()) {
                    if (file.isDirectory() && file.getName().equals(nomPackage)) {
                        this.listePackage( file, file.getName());
                    }
                }
            }

        } catch(Exception io){
            io.printStackTrace();
        }
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }


    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public Vector<Class<?>> getListeClasse() {
        return this.listeClasse;
    }

    public void setListeClasse(Vector<Class<?>> listeClasse) {
        this.listeClasse = listeClasse;
    }



    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try{
            response.setContentType("text/plain");
            out.println("URL = " + request.getRequestURI().substring(request.getContextPath().length()));
            out.println("Method = " + request.getMethod().toString());
            out.println();
            Mapping mapping1 = (Mapping)this.getMappingUrls().get(request.getRequestURI().substring(request.getContextPath().length()));
            if(mapping1!=null){
                out.println("url====>" + request.getRequestURI().substring(request.getContextPath().length()) + "==== >>>> classe = " + mapping1.getClassName());
                out.println("url====>" + request.getRequestURI().substring(request.getContextPath().length()) + "==== >>>> method = " + mapping1.getMethod());
                for (Class<?> class1 : this.getListeClasse()) {
                    if(class1.getSimpleName().equals(mapping1.getClassName())) {
                        out.println(class1.getSimpleName());
                        Object object = class1.newInstance();
                        Map<String, String[]> params = request.getParameterMap();
                        object = this.makaParametreDonnees(object, params, class1); //maka an'ilay parametre avy any @ JSP
                        Method[] methods = class1.getDeclaredMethods();
                        for (Method method : methods) {
                            if(method.getName().equals(mapping1.getMethod())) {
                                out.println(method.getName());
                                ModelView view = (ModelView)method.invoke(object);
                                out.println("View = " + view.getView());
                                if(view.getData()!=null) {
                                    for (Map.Entry<String, Object> entry : view.getData().entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();
                                        request.setAttribute(key, value);
                                    }
                                }
                                RequestDispatcher dispatcher = request.getRequestDispatcher("/"+view.getView());
                                dispatcher.forward(request, response);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception io) {
            out.println("Erreur aki = " + io.getMessage());
            io.printStackTrace();
        }
    }

    public void listePackage(File dossier, String packages) {
        try{
            String packageName = packages;
            for (File file : dossier.listFiles()) {
                if (file.isDirectory()) {
                        packages = packages + "." + file.getName();
                        this.listePackage(file, packages);
                }
                else{
                    this.getClass(file, packages);
                }
                packages = packageName;
            }


        } catch(Exception io){
            io.printStackTrace();
        }
    }

    public void getClass(File fichier, String packages) throws Exception {
        String name = fichier.getName();
        name = name.split(".class")[0];
        Class<?> classe = Class.forName( packages+"."+name);
        this.getListeClasse().add(classe);
        Method[] methods = classe.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(Url.class)){
                Mapping mapping = new Mapping(name, method.getName());
                this.getMappingUrls().put( "/"+name+""+method.getAnnotation(Url.class).name(), mapping);
            }
        }
        if(classe.getSuperclass()!=null){
            classe = classe.getSuperclass();
            methods = classe.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Url.class)){
                    Mapping mapping = new Mapping(name, method.getName());
                    this.getMappingUrls().put( "/"+name+""+method.getAnnotation(Url.class).name(), mapping);
                }
            }
        }
    }

    public String changeFirstAName(String nom){
		return nom.substring(0,1).toUpperCase() + nom.substring(1);
	}

    public Object makaParametreDonnees(Object object, Map<String, String[]> params, Class<?> class1) throws Exception{
        if(params.isEmpty()==false) {
            for (String paramName : params.keySet()) {
                String[] values = params.get(paramName);
                Object reponse = null;
                if(values!=null && values.length == 1){
                    reponse = (Object)values[0];
                    Field champ = class1.getDeclaredField(paramName);
                    if(champ!=null) {
                        String nomMethode =  "set" + this.changeFirstAName(paramName);
                        Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                        reponse = castValue(champ.getType(), values[0]);
                        setter.invoke(object, reponse); 
                    }
                } 
                
                else if(values!=null && values.length > 1) {
                    reponse = (Object)values;
                    Field champ = class1.getDeclaredField(paramName);
                    if(champ!=null) {
                        String nomMethode =  "set" + this.changeFirstAName(paramName);
                        Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                        reponse = liste(champ.getType(), values);
                        setter.invoke(object, reponse); 
                    }
                }     
            }
        }
        return object;
    }

    public Object castValue(Class<?> type, String value) throws Exception{
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type.toString() == "java.sql.Date") {
            return java.sql.Date.valueOf(value);
        }else if (type == Timestamp.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new java.sql.Timestamp(formatter.parse(value).getTime());
        }else if(type == Time.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            return new java.sql.Time(formatter.parse(value).getTime());
        }else {
            return null;
        }
    }

    public Object liste(Class<?> type, String[] value){
        if (type == Integer.class || type == int.class) {
            int[] liste = new int[value.length];
            for(int i=0; i<value.length; i++) {
                liste[i] = Integer.parseInt(value[i]);
            }
            return liste;
        }
        else if (type == Double.class || type == double.class) {
            double[] liste = new double[value.length];
            for(int i=0; i<value.length; i++) {
                liste[i] = Double.parseDouble(value[i]);
            }
            return liste;
        }
        else {
            return null;
        }
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try{
        processRequest(request, response);
//        1 - Maka ny parametre nalefa raha misy
//        2 - controlle de valeur na instanciation objet raha ilaina

//        3 - miset ny attribut ho an redirection raha misy
           

//        4 - Redirection
           
        } catch(Exception io) {
            out.print(io.getMessage());
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        processRequest(request, response);
        //        1 - Maka ny parametre nalefa raha misy
        //        2 - controlle de valeur na instanciation objet raha ilaina
        
        //        3 - miset ny attribut ho an redirection raha misy
                   
        
        //        4 - Redirection
              
    }
}
