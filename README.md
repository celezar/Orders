Application can using MySQL or H2 database. MySQL connection settings can be found in file src/main/resources/application.yml.
If commented out it will default to in memory H2 database.

Project is started by running command "mvn spring-boot:run" from the root. It can be accessed on port 8080.
APIs can be tested using url http://localhost:8080/swagger-ui.html. API documentation is also contained within that page.
Also application health can be monitored using url http://localhost:8080/actuator.

Application can be build using command "mvn package" and it will create orders-0.0.1-SNAPSHOT.jar file in target directory.
Then it can be run with "java -jar orders-0.0.1-SNAPSHOT.jar"


TODO: More tests and input validation.

