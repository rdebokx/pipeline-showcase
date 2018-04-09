# pipeline-showcase

##Goal
The goal of this project is to showcase the use of the following systems / framework in Kotlin. Therefore, 
this particular combination of frameworks might not make sense, though this is simply because this is aimed at 
demonstrating the use of these frameworks.

- Docker
- Kafka (TODO)
- Akka
- Gradle
- inFluxDB (TODO)
- Grafana (TODO)
- Spring

##Dependencies
- Gradle 4.0 or higher (`brew install gradle` on OSX)

##Running the application

### Step 1: Starting Kafka Zookeeper
`./bin/zookeeper-server-start.sh ../pipeline-showcase/config/zookeeper.properties`

TODO: start kafka servers

TODO: automate this in one central script

### Step 2: starting the application

####Docker
The preferred way to run the application is by using Docker. This can be done by following these two steps
1. Building the application: `./gradlew docker build`
2. Running the application: `docker run -p 8080:8080 -t com.rdebokx/pipeline-showcase` 

The application will then be available on `localhost:8080`

####Native
If for some reason you really prefer to run the application natively, you can do so by running
`./gradlew bootRun`