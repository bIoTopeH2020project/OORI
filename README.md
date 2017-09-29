# OORCS - An O-MI/O-DF RDF Conversion Service

OORCS is a service that converts O-DF structures into RDF format using the [O-DF ontology](src/main/resources/ODF-Ontology.ttl).

## Building the project

At the to a command prompt and move to the project directory and run the command

```
mvn clean package 
```

This will give you an executable jar file in the directory ```target```. If you have docker installed on your machine, you can also create a docker image from the source code by issuing the command

```
mvn clean package docker:build 
```

### Running the Server without Docker

The service is packaged as a ```.jar``` file. To run it, simply execute the generated jar file using the following command:

```
java -jar oorcs-0.0.1-SNAPSHOT.jar
```

The filename varies depending on the current version of the project. The command will start the OORCS server at the default port 8080.

## Usage

OORCS provides the HTTP method ```/toRDF``` which expects an O-MI/O-DF response in the POST body and returns the RDF TTL representation in the HTTP response.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://projects.spring.io/spring-boot/) - Application Framework
* [docker-maven-plugin](https://github.com/spotify/docker-maven-plugin) - Docker Image Creation

## Authors

* **Christian Mader** - *Initial design and implementation*

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

This work was funded by the [bIoTope project](http://biotope.cs.hut.fi/).