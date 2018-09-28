FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD picking-0.0.1-SNAPSHOT.jar app.jar
# All Spring Boot components expose 9093 as the container java debug port
EXPOSE 9093
EXPOSE 8000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=dev","-jar","/app.jar"]