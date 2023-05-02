#front-servlet
Alaina ilay fw.jar de hatao anaty "class Path"

Soratana ao amn web.xml anao
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu2068.framework.servlet.FrontServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

    <context-param>
        <param-name>NomduPackage</param-name>
        <!-- Remplacez NomduPackage par le nom de votre package ou on prend tous les models que vous aviez creer et ne touche a rien SVP! -->
        <param-value>models</param-value> 
    </context-param>

Rehefa te hampifandray page amin ny class anakiray ah de:
    * ra ohatra ka setters zao ilay izy de mila hatao mitovy anarana @ attribut(s) @n'ilay class fotsiny le name an'ilay parametre ao amin ilay page
        ex: 
            Class Test {
                int id;
                String name;

                void setId(int id) { this.id = id; }
                void setName(String name) { this.name = name;}
            }
            Dans jsp: 
            <input type="number" name="id">
            <input type="text" name="name">

    * ra ohatra ka method tsotra fotsiny indray ilay izy anh de importena ilay fichier "etu2068.annotations.Url" ao @n ilay class de asina annotation
    ilay methode mba ahafatarana oe ito ilay methode atsoina avy any amin ilay page, fanoranatana azy ==> name= "/" + nom_tinao_omena_azy
        ex: 
        Class Test {
            @Url(name = "/save")
            ModelView save() {  ..traitement..  }
        }

    De ny url soratana eny ambony indray de: "Test/save" de tonga any amin ilay fonction save izy avy eo.
    ==> ny fanoratana ny URL anh le "nom_class" + "" + "name_anotation_url"

    * ra ohatra ka mila argument ilay method:
        + mila mi-import "etu2068.annotations.Argument"
        + asina annotation @Argument ilay Argument @'ilay Method
        ex: 
            Class Test {
                @Url(name = "save")
                ModelView save(@Argument(name = "a")int argument1, @Argument(name = "b")String argument2, @Argument(name = "liste")int[] argument3) { 
                     //..traitement..  
                }
            }
        + hatao mitovy anarana @name an ilay anotation ao @ilay argument ny name an ilay parametre any @page 
        ex dans jsp:
            <html>
                <input type="number" name="a">
                <input type="text" name="b">
                <input type="number" name="liste">
                <input type="number" name="liste">
            </html>
    de omena iany avy eo ny url ahazahoana azy

Toute fonction ze mifandray @page de mila mi-retourne valeur de type ModelView:
    * importena aloha etu2068.modelView.ModelView;
    * de set-tiavana ilay attribu view ao aminy ==> model.setView("view") ==> hatao ao ilay URL vao2 handefasana azy
    * afaka mandefa donnees bdb koa makany @page handefasana azt, mandray type object ilay attribu data ao ==> model.addItem("anarana_andraisana_azy_any_am_page", donnees_alefa);
    ex: 
        Class Test {
            @Url(name = "save")
            ModelView save(@Argument(name = "a")int a, @Argument(name = "b")String b, @Argument(name = "liste")int[] liste) { 
                ModelView model = new ModelView("Compte.jsp");
                List<Compte> liste = (List<Compte>)new Compte().list(null);
                model.addItem("liste", liste);
                model.addItem("addition", (a+b));
                return model;
            }
        }
    
    Rehefa handray azy avy any @page de raisina otran fandraisana request.getAttribute("le_nom_nandefasana_donnees_tany_amn_modelView") ilay donnees nalefa rehetra
    ex dans une page jsp:
        <%  List<Test> liste = (List<Test>)request.getAttribute("liste"); %>


