FROM openjdk:11
ARG JAR_FILE=*.jar
COPY target/${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8070