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
                    if(class1.getSimpleName().equals( mapping1.getClassName())) {
                        out.println(class1.getSimpleName());
                        Object object = class1.newInstance();
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
