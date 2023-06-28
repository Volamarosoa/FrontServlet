package etu2068.framework.servlet;
import javax.servlet.*;
import javax.servlet.http.*;

import etu2068.annotations.Url;
import etu2068.annotations.Argument;
import etu2068.annotations.Singleton;
import etu2068.modelView.ModelView;

import java.io.*;
import java.lang.Thread;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Set;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import etu2068.framework.Mapping;
import java.lang.ClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import javax.servlet.annotation.MultipartConfig;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.InvocationTargetException;


@MultipartConfig
public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> mappingUrls;
    Vector<Class<?>> listeClasse;
    HashMap<String, Object> instances;

    public void init() {
        try{
            this.setMappingUrls(new HashMap<String,Mapping>());
            ClassLoader classLoader = new Thread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");
            this.listeClasse = new Vector<Class<?>>();
            this.instances = new HashMap<String, Object>();

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


    public HashMap<String, Object> getInstances() {
        return this.instances;
    }

    public void setInstances(HashMap<String, Object> instances) {
        this.instances = instances;
    }

    public void addInstance(String key, Object value) {
        this.getInstances().put(key, value);
    }

    //test si c'est un fichier
    private boolean isFilePart(Part part) {
        String disposition = part.getHeader("content-disposition");
        return (disposition != null && disposition.contains("filename"));
    }

    //retourne les valeurs de ce qui ne sont pas un fichier
    private String[] readValueFromPart(Part part) throws IOException {
        InputStream partContent = part.getInputStream();
        InputStreamReader reader = new InputStreamReader(partContent, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }
    

    //retourne les valeurs en byte de ce qui sont fichier
    private byte[] readBytesFromPart(Part part) throws IOException {
        InputStream partContent = part.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = partContent.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        return output.toByteArray();
    }

    public void resetFieldToNull(Object object) throws Exception {
        String reinitialiser = getServletContext().getInitParameter("reinitialiser");
        Class<?> objectClass = object.getClass();
        Method[] methods = objectClass.getDeclaredMethods();
        for(Method method : methods) {
            if(method.getName().equals(reinitialiser)) {
                method.invoke(object);
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String contentType = request.getContentType();
        try{
            response.setContentType("text/plain");
            out.println("URL = " + request.getRequestURI().substring(request.getContextPath().length()));
            out.println("Method = " + request.getMethod().toString());
            out.println();
            for(int i = 0; i < this.getListeClasse().size(); i++) {
                if(this.getListeClasse().get(i).isAnnotationPresent(Singleton.class)) {
                    out.println("class: " + this.getListeClasse().get(i).getSimpleName());
                }   
                else{
                    out.println("tisy: " + this.getListeClasse().get(i).getSimpleName());
                }
            }

            out.println("taille: " + this.getInstances().size());

            Mapping mapping1 = (Mapping)this.getMappingUrls().get(request.getRequestURI().substring(request.getContextPath().length()));
            if(mapping1!=null){
                out.println("url====>" + request.getRequestURI().substring(request.getContextPath().length()) + "==== >>>> classe = " + mapping1.getClassName());
                out.println("url====>" + request.getRequestURI().substring(request.getContextPath().length()) + "==== >>>> method = " + mapping1.getMethod());
                for (Class<?> class1 : this.getListeClasse()) {
                    if(class1.getSimpleName().equals(mapping1.getClassName())) {
                        out.println(class1.getSimpleName());
                        Object object = null;
                        if(this.getInstances().containsKey(class1.getSimpleName())) {
                            object = this.getInstances().get(class1.getSimpleName());
                            if(object == null) {
                                out.println("mbola tsisy");
                                object = class1.newInstance();
                                this.getInstances().replace(class1.getSimpleName(), object);
                            }
                            else {
                                this.resetFieldToNull(object);
                            }
                        }
                        else{
                            out.println("misy ve? tsisy");
                            object = class1.newInstance();
                        }
                        Map<String, String[]> params = request.getParameterMap();
                        if (contentType != null && contentType.startsWith("multipart/")) {
                            this.makaParametreDonneesAvecFichier(object, request, class1, out); //maka an'ilay parametre setters any jsp fa avec setters
                        } else {
                            object = this.makaParametreDonnees(object, params, class1); //maka an'ilay parametre setters any jsp
                        }
                        Method[] methods = class1.getDeclaredMethods();
                        for (Method method : methods) {
                            if(method.getName().equals(mapping1.getMethod())) {
                                Object[] argument = this.mamenoParametreMethode(method, params);    //mameno parametre an'ilay fonction                                 
                                ModelView view = null;
                                try {
                                if(argument != null){
                                    view = (ModelView)method.invoke(object, argument);
                                }
                                else{
                                    view = (ModelView)method.invoke(object);
                                }
                                // Code où l'exception InvocationTargetException se produit
                                } catch (InvocationTargetException e) {
                                    Throwable cause = e.getCause();
                                    if (cause != null) {
                                        // Gérez ou affichez l'exception réelle
                                        cause.printStackTrace();
                                    }
                                }
                                
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
            out.println("Erreur aki = " + io);
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
        Class<?> classe = Class.forName( packages + "." + name );
        this.getListeClasse().add(classe);

    //prends tous les noms des methodes ou il y a des annotation URL    
        
    Method[] methods = classe.getDeclaredMethods();
    for (Method method : methods) {
        if(method.isAnnotationPresent(Url.class)){
            Mapping mapping = new Mapping(name, method.getName());
                this.getMappingUrls().put( "/"+name+""+method.getAnnotation(Url.class).name(), mapping);
            }
        }
        
        if(classe.getSuperclass() != null){
            Class<?> c = classe.getSuperclass();
            methods = c.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Url.class)){
                    Mapping mapping = new Mapping(name, method.getName());
                    this.getMappingUrls().put( "/"+name+""+method.getAnnotation(Url.class).name(), mapping);
                }
            }
        }
        
        if( classe.isAnnotationPresent(Singleton.class) ) {
            this.getInstances().put(name, null);
        }    

    }

    public String changeFirstAName(String nom){
		return nom.substring(0,1).toUpperCase() + nom.substring(1);
	}

// ty le fonction manao setters anle objet 
    public Object makaParametreDonnees(Object object, Map<String, String[]> params, Class<?> class1){
        PrintWriter out = new PrintWriter(System.out);
        if(params.isEmpty()==false) {
            for (String paramName : params.keySet()) {
                String[] values = params.get(paramName);
                Object reponse = null;
                if(values!=null && values.length == 1){
                    reponse = (Object)values[0];
                    try{
                        Field champ = class1.getDeclaredField(paramName);
                        String nomMethode =  "set" + this.changeFirstAName(paramName);
                        Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                        reponse = castValue(champ.getType(), values[0]);    //micaste anle valiny io fonction io
                        setter.invoke(object, reponse); 
                    }
                    catch(Exception io){
                        io.printStackTrace();
                    }
                } 
                
                else if(values!=null && values.length > 1) {
                    reponse = (Object)values;
                    try{
                        Field champ = class1.getDeclaredField(paramName);
                        String nomMethode =  "set" + this.changeFirstAName(paramName);
                        Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                        reponse = liste(champ.getType(), values);
                        setter.invoke(object, reponse); 
                    }
                    catch(Exception io) {
                        io.printStackTrace();
                    }
                }     
            }
        }
        return object;
    }

// ty le fonction manao setters anle objet avec un fichier
    private Object makaParametreDonneesAvecFichier(Object objet, HttpServletRequest request, Class<?> class1, PrintWriter out) throws IOException{
        try{
            Collection<Part> parts = request.getParts();     
            for(Part part : parts) {
                if(isFilePart(part)) {
                    String partName = part.getName();
                    out.println("fichier: " + partName);
                    byte[] fileBytes = readBytesFromPart(part);
                    Field champ = class1.getDeclaredField(partName);
                    String nomMethode =  "set" + this.changeFirstAName(partName);
                    Method setter = class1.getDeclaredMethod(nomMethode, byte[].class);
                    setter.invoke(objet, fileBytes); 
                    out.println("eto aloha " + nomMethode + " type = " + champ.getType());
                }else{
                    Object reponse = null;
                    String partName = part.getName();
                    out.println("simple: " + partName);
                    String[] partValue = readValueFromPart(part);
                    if(partValue!=null && partValue.length == 1){
                        try{
                            Field champ = class1.getDeclaredField(partName);
                            String nomMethode =  "set" + this.changeFirstAName(partName);
                            Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                            reponse = castValue(champ.getType(), partValue[0]);    //micaste anle valiny io fonction io
                            setter.invoke(objet, reponse); 
                        }
                        catch(Exception io){
                            io.printStackTrace();
                        }
                    } 
                    
                    else if(partValue!=null && partValue.length > 1) {
                        try{
                            Field champ = class1.getDeclaredField(partName);
                            String nomMethode =  "set" + this.changeFirstAName(partName);
                            Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                            reponse = liste(champ.getType(), partValue);
                            setter.invoke(objet, reponse); 
                        }
                        catch(Exception io) {
                            io.printStackTrace();
                        }
                    }
                }
            }
        }
        catch(Exception io) {
            io.printStackTrace();
            out.println(io.getMessage());
        }
        return objet;
    }

    public Object[] mamenoParametreMethode(Method method, Map<String, String[]> params) throws Exception{
        Object[] arguments = null;
        if(params.isEmpty()==false){
            Parameter[] parameters = method.getParameters();
            if(parameters.length != 0) {
                arguments = new Object[parameters.length];
                int i = 0;
                for (Parameter parameter : parameters) {
                    for (String paramName : params.keySet()) {
                        if(paramName.equals(parameter.getAnnotation(Argument.class).name())) {
                            String[] values = params.get(paramName);
                            Object reponse = null;
                            if(values!=null && values.length == 1){
                                arguments[i] = castValue(parameter.getType(), values[0]);
                            } 
                            
                            else if(values!=null && values.length > 1) {
                                arguments[i] = liste(parameter.getType(), values);
                            }  
                        }   
                    }
                    i++;
                }     
            }
        }
        return arguments;
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
        if (type == String.class) {
            return value;
        }
        else if (type == Integer.class || type == int.class) {
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
