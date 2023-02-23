# Spring Boot Application Example
-This is a sample Java / Maven / Spring Boot application which provides RESTful services (GET/POST/PUT/PATCH).
-This application is packaged as a war which has Tomcat 8 embedded. No Tomcat installation is necessary. You run it using the java -jar command. "WebappApplication" is the main class click on it as run as java application , it will use port 8080 . In case the port is already in use then open cmd and input commands 1) netstat -a -n -o | find "8080" 2) taskkill /F /PID "tcp" number coming from above to kill the method.

After successful start of the application, Select GET option and mention the URL - localhost:8080/healthz to check the first assignment. For second assignment see URL- http://localhost:8080/v1/user , here you can get ,and put,patch in form of {

"first_name": "Jatin",

"last_name": "Parmar",

"password": "Jatin@07",

"username": "jatinparmar@gmail.com"

} - data is sent in json format , created and updated date are added

Added Product class too and linked with the UserInfo table 

passwords are crypted in order to protect it using BCrypt mvn test -Dtest='TestClassName'.

select queries using , MysqlWorkbecnch to check the database and the table , table user js created when the sofwate is run

Demo Test........
