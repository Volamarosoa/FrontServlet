package etu2068.framework.servlet;
import javax.servlet.*;
import javax.servlet.http.*;

import dao.annotations.Url;

import java.io.*;
import java.lang.Thread;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import etu2068.framework.Mapping;
import java.lang.ClassLoader;
import java.util.Enumeration;
import java.net.URL;
public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> mappingUrls;

    public void init() {
        try{
            this.setMappingUrls(new HashMap<String,Mapping>());
            ClassLoader classLoader = new Thread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File dir = new File(url.toURI());
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
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


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        out.println("URL = " + request.getRequestURI().substring(request.getContextPath().length()));
        out.println("Method = " + request.getMethod().toString());

        // for( Map.Entry<String, Mapping> sets : this.getMappingUrls().entrySet() ){
        //     Mapping mapping = (Mapping)sets.getValue();
        //     out.println("url====>" + sets.getKey() + "==== >>>> classe = " + mapping.getClassName());
        //     out.println("url====>" + sets.getKey() + "==== >>>> method = " + mapping.getMethod());
        // }

        out.println();

        Mapping mapping1 = (Mapping)this.getMappingUrls().get(request.getRequestURI().substring(request.getContextPath().length()));
        out.println("url====>" + request.getRequestURI().substring(request.getContextPath().length()) + "==== >>>> classe = " + mapping1.getClassName());
        out.println("url====>" + request.getRequestURI().substring(request.getContextPath().length()) + "==== >>>> method = " + mapping1.getMethod());
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
        if(classe.getSuperclass()!=null){
            classe = classe.getSuperclass();
        }
        Method[] methods = classe.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(Url.class)){
                Mapping mapping = new Mapping(name, method.getName());
                this.getMappingUrls().put( "/"+name+""+method.getAnnotation(Url.class).name(), mapping);
            }
        }
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        processRequest(request, response);
//        1 - Maka ny parametre nalefa raha misy
//        2 - controlle de valeur na instanciation objet raha ilaina

//        3 - miset ny attribut ho an redirection raha misy
           

//        4 - Redirection
      
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
