FROM registry.cirrus.ibm.com/cio-ci-cd/java17-maven-image:1.0.2 AS builder

USER root

COPY pom.xml .

RUN mvn -T $(nproc --all) -ntp dependency:go-offline

COPY src src

RUN mvn -T $(nproc --all) -ntp package -Dskiptests

FROM registry.cirrus.ibm.com/cio-ci-cd/java17-maven-image:1.0.2 AS layers

USER root

COPY --from=builder target/spring-secrets-manager.jar .
RUN java -Djarmode=layertools -jar spring-secrets-manager.jar extract

FROM registry.cirrus.ibm.com/cio-ci-cd/java17-maven-image:1.0.2

USER root

COPY --from=layers dependencies/ .
COPY --from=layers snapshot-dependencies/ .
COPY --from=layers spring-boot-loader/ .
COPY --from=layers application/ .

RUN dnf update -y

CMD ["java", "org.springframework.boot.loader.JarLauncher"]

USER 1001
