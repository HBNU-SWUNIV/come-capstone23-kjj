FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/zanbanzero-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java", "-jar", "/docker-springboot.jar"]