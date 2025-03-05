# Use OpenJDK 17 as the base image (or match your Java version)
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/crud-springboot-app-*.jar app.jar

# Expose the port your app runs on (default Spring Boot port is 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]


# docker buildx build .
#steps to run the docker image
#   mvn clean package
#   docker build -t crud-springboot-app:latest .
#docker stop crud-springboot-app-container
#docker rm crud-springboot-app-container
#   docker run -d -p 8080:8080 --name crud-springboot-app-container --network my-network crud-springboot-app:latest
#With Docker Compose, you replace these with:
#mvn clean package
#Initial setup:
#docker-compose up -d (or docker-compose up for foreground).
#mvn clean package
# docker-compose up -d --build

#connect to db using docker
#docker exec -it mysql-container mysql -u root -p1234 sakila
#show tables