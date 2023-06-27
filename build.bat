cd Framework 
javac -d . *java
jar cf fw.jar .
mv fw.jar ..
cd ..
mv fw.jar Test-Framework/WEB-INF/lib
cd Test-Framework/WEB-INF/classes
javac -d . *java
cd ../../..
mkdir Temporaire
cp -r Test-Framework/* Temporaire/
cd Temporaire
jar cf Temporaire.war .
cp Temporaire.war C:/'Program Files'/'Apache Software Foundation'/'Tomcat 9.0'/webapps