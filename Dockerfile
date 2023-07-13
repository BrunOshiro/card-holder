# Define a imagem base
FROM openjdk:17
WORKDIR /app
COPY target/card-holder-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "card-holder-0.0.1-SNAPSHOT.jar"]