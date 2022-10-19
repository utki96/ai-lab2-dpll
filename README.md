## System requirements to build and run the project:

1. Apache Maven - (3.6.0)
2. JDK - (1.8)
3. The code has been tested on `crackle1` server

## Steps:

1. Change the working directory to the root folder of this project.
2. Execute command: `mvn clean`
3. Execute command: `mvn package`
4. Now you have an executable jar in *"./target/lab2-dpll-1.jar"*
5. Run this jar file by this command:\
   ```java -jar target/lab2-dpll-1.jar [-v] nColors file_path```

Eg: ```java -jar target/lab2-dpll-1.jar -v 4 /home/utkarshtyg/Documents/us/us48.txt```

###
### Important points about passing arguments:

1. All arguments should be passed after the name of the jar file separated by spaces
2. Argument [-v] is optional but [ nColors, file_path] are mandatory
3. _file_path_ should be the last argument passed
4. _nColors_ should be an integer and the 2nd last config, just before file_path
5. Arg -v is optional and should be passed just before the other 2
