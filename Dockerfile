FROM maven:3.9.9-eclipse-temurin-21 as build
WORKDIR /app

COPY pom.xml ./
COPY ./src ./src/

RUN mvn clean install -DskipTests

EXPOSE 8080
EXPOSE 35729

ENV DEFAULT_CMD="./mvnw spring-boot:run -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"
ENV TEST_CMD="./mvnw test"

CMD ["/bin/bash", "-c", "if [[ $PROFILE == 'test' ]]; then ${TEST_CMD}; else ${DEFAULT_CMD}; fi"]
