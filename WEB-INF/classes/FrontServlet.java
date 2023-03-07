package etu2068.framework.servlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;



public class FrontServlet extends HttpServlet{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("URL = " + request.getRequestURI());
        out.println("Method = " + request.getMethod().toString());
        String nom = request.getQueryString(); 
        String[] tab = request.getRequestURI().split("/");
        if(nom.equals("") == false){
            out.println(tab[tab.length - 1] + "?" + nom);
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
