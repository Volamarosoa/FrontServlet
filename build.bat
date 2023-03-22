cd Framework 
javac -d . *java
jar cf fw.jar .
mv fw.jar ..
cd ..
mv fw.jar Test-Framework/WEB-INF/lib
cd Test-Framework/WEB-INF/classes
javac -d . *java
cd ../../
jar cf Test-Framework.war .