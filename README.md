# Traffic Simulator
by The Waypointers

[![Build Status](https://travis-ci.org/TheWaypointers/TrafficSimulator.svg?branch=dev)](https://travis-ci.org/TheWaypointers/TrafficSimulator) **dev**

[![Build Status](https://travis-ci.org/TheWaypointers/TrafficSimulator.svg?branch=master)](https://travis-ci.org/TheWaypointers/TrafficSimulator) **master**

## Building and running

*Note:* Binaries are also available [here](https://github.com/TheWaypointers/TrafficSimulator/releases/tag/v1.0).

The application runs on Java 8. It is supposed to be built with Maven. Just execute the following command in the root directory of the project:
```
mvn clean install
```
It will download all the necessary dependencies, run tests and build the executable. The executable JAR archive can be found under 
```
/target/TrafficSimulator-1.0-FINAL-jar-with-dependencies.jar
```
To run, it also needs the `roadmap.xml` file in the same directory (current working directory) to read the road network from it. An example road map can be found in the root directory of the project.
