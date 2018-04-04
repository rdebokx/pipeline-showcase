# pipeline-showcase

##Dependencies
- Gradle 4.0 or higher (`brew install gradle` on OSX)

##Running the application

####Docker
The preferred way to run the application is by using Docker. This can be done by following these two steps
1. Building the application: `./gradlew docker build`
2. Running the application: `docker run -p 8080:8080 -t com.rdebokx/pipeline-showcase` 

The application will then be available on `localhost:8080`

####Native
If for some reason you really prefer to run the application natively, you can do so by running
`./gradlew bootRun`