# CRUD Spring Boot Application

This project is a simple CRUD (Create, Read, Update, Delete) application built with Spring Boot. It manages a list of actors stored in a MySQL database.

## Project Structure

```
crud-springboot-app
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── CrudSpringbootAppApplication.java
│   │   │           ├── controller
│   │   │           │   └── ActorController.java
│   │   │           ├── model
│   │   │           │   └── Actor.java
│   │   │           ├── repository
│   │   │           │   └── ActorRepository.java
│   │   │           └── service
│   │   │               └── ActorService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── CrudSpringbootAppApplicationTests.java
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository:**
   ```
   git clone <repository-url>
   cd crud-springboot-app
   ```

2. **Configure the database:**
   Update the `src/main/resources/application.properties` file with your MySQL database details:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/sakila
   spring.datasource.username=root
   spring.datasource.password=1234
   ```

3. **Build the project:**
   Use Maven to build the project:
   ```
   mvn clean install
   ```

4. **Run the application:**
   Start the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

## Usage

- The application exposes RESTful endpoints for managing actors.
- You can perform CRUD operations using tools like Postman or cURL.

## Dependencies

This project uses the following dependencies:
- Spring Boot
- Spring Data JPA
- MySQL Connector

## Testing

Test cases are included in the `src/test/java/com/example/CrudSpringbootAppApplicationTests.java` file to ensure the application behaves as expected. Run the tests using:
```
mvn test
```

## License

This project is licensed under the MIT License.

swagger url : http://localhost:8080/swagger-ui.html

Step-by-Step Process
1. Rebuild the Docker Image
   Since your docker-compose.yml uses the image crud-springboot-app:latest, you need to rebuild this image with your updated code:

Navigate to Your Project Directory: Ensure you’re in the directory containing your Dockerfile and project files (e.g., C:\development\hibernateJpa\crud-springboot-app):

cd C:\development\hibernateJpa\crud-springboot-app
Build the New Image: Run the following command to build the updated image. Replace . with the path to your project context if your Dockerfile is elsewhere:
docker build -t crud-springboot-app:latest .

mvn clean package
Verify the Image: Check that the new image is built successfully:
docker images
3. Redeploy the Stack
   docker stack deploy -c docker-compose.yml crud-stack
4. Verify the Update: Check the service status to ensure the update was successful:
docker service ls


docker build -t crud-springboot-app:latest .
mvn clean package
docker images
docker stack deploy -c docker-compose.yml crud-stack
docker service ls



mvn clean package  # Rebuild the JAR if needed
docker build -t crud-springboot-app:latest .
Or
docker tag crud-springboot-app:v1.0.1 crud-springboot-app:v1.0.1
