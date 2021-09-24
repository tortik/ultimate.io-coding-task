FROM adoptopenjdk/openjdk11:alpine AS build
WORKDIR /usr/src/app
COPY ./ .
RUN ./gradlew --version
RUN ./gradlew --no-daemon build

FROM adoptopenjdk/openjdk11:alpine
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/build/libs/web-server-task-0.0.1-SNAPSHOT.jar web-server-task.jar
EXPOSE 9000 8080
ENTRYPOINT ["java","-jar","web-server-task.jar"]