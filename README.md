# yacrud
Yet Another CRUD application

Instructions:

1. Install Java 8
2. Install Maven 3
3. Build from project root using Maven 3, e.g.: 
   ```
   mvn clean install
   ``` 
4. Start the server JAR using Java, e.g.: 
   ```
   java -jar target/yacrud-server-1.0-SNAPSHOT.jar
   ```
5. Open http://localhost:8080/ in your browser
6. Enjoy!


# Functionality

1. Filter/sort through available predefined customers.
2. Double-click a customer to edit their status. 
3. Select a customer from the list to see/edit notes.
4. When no note is selected, use the available form to add a new note for the selected customer.
5. Select a note to edit it using the same form. 
6. Note is added or modified on "Save".
7. Any unsaved modifications are undone on "Reset".


# Structure 

This Application consists of several key parts:
1. Spring Boot Application/Configuration that puts everything together with minimum boilerplate.
   Actuator-based monitoring, improved logging (Sleuth), etc., are trivial to add at this point. 
2. Basic JPA model + matching repositories. 
   Additionally, repositories are enhanced to work with Vaadin query objects, 
   instead of trying to translate standard Spring Pageable.
   Also basic in-memory SQL database is used for simplicity.
3. Vaadin UI with actual UI being done via code, not WYSIWIG designer.
   The UI is separated into the main UI initialization with event glue and several independent UI components.  
4. Lombok for cutting off some boilerplate.
5. No tests, because, let's be honest, I don't care about maintaining this or ever running this again.
