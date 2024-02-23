# OpenJDK  image 
FROM openjdk:11 

# Set the working directory to /app 
WORKDIR /app 
# Copy the current directory contents into the container at /app 
COPY target/*.jar /app/conference-booking-service.jar 

# Make port 8080 available to the world outside this container 
EXPOSE 8080 

# Run your application using the JAR file 
CMD ["java", "-jar", "conference-booking-service.jar"]