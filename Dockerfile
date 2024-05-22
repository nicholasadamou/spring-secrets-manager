# Use openjdk and maven official images from DockerHub

FROM maven:3.8-openjdk-17 AS builder

# Switch to root user
USER root

# Copy the POM file and download necessary dependencies
COPY pom.xml .

RUN mvn -T $(nproc) -ntp dependency:go-offline

# Copy the source code and build the application
COPY src src

RUN mvn -T $(nproc) -ntp package -DskipTests

# Use openjdk official image from DockerHub for the layers stage
FROM openjdk:17-jdk AS layers

# Switch to root user
USER root

# Copy the generated jar from the builder stage and extract layers
COPY --from=builder target/spring-secrets-manager.jar .

RUN java -Djarmode=layertools -jar spring-secrets-manager.jar extract

# Use openjdk official image from DockerHub for the final stage
FROM openjdk:17-jdk

# Switch to root user
USER root

# Copy the extracted layers and update dependencies
COPY --from=layers dependencies/ .
COPY --from=layers snapshot-dependencies/ .
COPY --from=layers spring-boot-loader/ .
COPY --from=layers application/ .

RUN apt-get update -y && apt-get upgrade -y

CMD ["java", "org.springframework.boot.loader.JarLauncher"]

# Switch to non-root user for security
USER 1001
