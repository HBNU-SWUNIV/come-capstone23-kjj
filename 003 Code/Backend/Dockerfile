FROM gradle:7.5.1-alpine AS BUILD_DIR
WORKDIR /home/gradle/src/

COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

COPY . .
RUN gradle clean bootJar


FROM openjdk:17-jdk

COPY --from=BUILD_DIR /home/gradle/src/build/libs/zanbanzero-0.0.1-SNAPSHOT.jar /application/zanbanzero-0.0.1-SNAPSHOT.jar

WORKDIR /application

ENTRYPOINT ["java", "-jar", "/application/zanbanzero-0.0.1-SNAPSHOT.jar"]