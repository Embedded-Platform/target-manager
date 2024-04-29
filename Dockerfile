FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]