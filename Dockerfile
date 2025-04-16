FROM maven:3.9.9-eclipse-temurin-21 as build
WORKDIR /app

COPY pom.xml ./
COPY ./src ./src/

RUN mvn clean install -DskipTests

EXPOSE 8080
EXPOSE 35729

ENV DEFAULT_CMD="./mvnw spring-boot:run -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"

CMD ["/bin/bash", "-c", "${DEFAULT_CMD}"]
