FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean buildFatJar --no-daemon

FROM artifactory.mallgroup.com/docker/openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*all.jar /app/ktor-app.jar
ENTRYPOINT ["java","-jar","/app/ktor-app.jar"]