FROM gradle:9.1.0-jdk25-alpine AS build

WORKDIR /home/gradle

COPY . .

RUN gradle shadowJar

FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S scriletio && \
    adduser -S -H scriletio -G scriletio

WORKDIR /home/app

COPY --chown=scriletio:scriletio --from=build /home/gradle/build/libs/*-all.jar scriletio.jar

USER scriletio

ENTRYPOINT [ "java", "-jar", "scriletio.jar" ]