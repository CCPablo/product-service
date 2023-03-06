FROM maven:3.8.1-openjdk-17 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:17 AS app
COPY --from=build /usr/src/app/target/*.jar /usr/app/product-eas.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/product-eas.jar"]
