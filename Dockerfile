FROM openjdk:11
EXPOSE 8080
ARG JAR_FILE=target/assignment-0.0.1.jar
ADD ${JAR_FILE} assignment-0.0.1.jar
ENTRYPOINT ["java","-jar","/assignment-0.0.1.jar"]