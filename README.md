# SENG202-TEAM2

![Logo](https://eng-git.canterbury.ac.nz/frd15/SENG202-T2/raw/FP1-boiled-egg/logo.png)

### Running For Production
1. Ensure Java11 is installed on your machine.
2. Navigate to ```https://eng-git.canterbury.ac.nz/frd15/SENG202-T2```.
3. Click the download button on the right and select ``` Download 'generate_artifacts'```.
4. Extract the downloaded folder to a desired location.
5. Navigate to the ```/target``` folder in a terminal window. 6. Run the command ```java -jar mytruck-1.0-SNAPSHOT.jar```.

These steps will generate a new empty database for persistent data storage. If you would like to load an existing database, follow the steps in the <b>Load Existing MyTruck Database</b> section.

### Running as a Developer
1. Ensure Java11 is installed on your machine.
2. Open a terminal session and navigate to a desired location.
3. Run the command ```git clone https://eng-git.canterbury.ac.nz/frd15/SENG202-T2.git```.
4. Navigate into the new directory ```/SENG202-T2```.
5. Run the command ```mvn install```.
6. Open IntelliJ and import the project with maven and Java11.
7. Within IntelliJ, navigate to  ```src/main/java/```.
8. Right click on ```Main.java``` and select ```Run 'Main.main()'```.

These steps will generate a new empty database for persistent data storage. If you would like to load an existing database, follow the steps in the <b>Load Existing MyTruck Database</b> section.

### MyTruck Databases

##### Load Existing MyTruck Database
1. Complete steps 1-5 in the <b> Running For Production</b> section OR steps 1-7 the <b>Running as a Developer</b> section.
2. Place your existing MyTruck database at the same directory level as ```mytruck-1.0-SNAPSHOT.jar```.
3. Rename the database to exactly ```MyTruck.db```.
4. If you are running for production, run the command ```java -jar mytruck-1.0-SNAPSHOT.jar```.
5. If you are running as a developer, right click on ```Main.java``` and select ```Run 'Main.main()'```.

##### MyTruck Database with Filler Data 
If you would like to test the application without entering/uploading your own data, simply download a sample database from here ```http://mytruck.zies.net:8080/MyTruck.db``` and complete the steps in the <b>Load Existing MyTuck Database</b> section.

### Running Tests

This project uses JUnit and Cucumber for testing. Run the tests using this command, from the project root ```SENG202-T2```

```$bash
mvn test
```

### Documentation

Javadocs can be generated using `Maven`. Make sure to set the variable `JAVA_HOME` is set and run this command from the project root

```$bash
mvn javadoc:javadoc
```
The command will then generate javadoc html files in `target/site/apidocs`. Open the `index.html` file to view the documentations.

### User Profiles
MyTruck features a user account system and supports profiles with various level of authorisation. These permission levels are as follows:
``` 
Level 0, General employee -> access to sales, orders and settings screens.
Level 1, Analyst -> same access as level 0 and has access to the analytics screen.
Level 2, Manager -> same access as level 1 and has access to uploading and viewing screens.
Level 3, Owner -> same access as level 2 and can modify users.
``` 

### Authors

- Flynn Doherty (frd15)
- Josh Yee (jmy39)
- Kenzie Tandun (kta79)
- Martin Lopez Uribe (mlo53)
- Sam Sandri (sjs234)
- Seth Kingsbury (swk22)

### Acknowledgements

- University of Canterbury
