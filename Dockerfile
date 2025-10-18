FROM eclipse-temurin:25-jdk-alpine AS build

COPY . .

RUN ./gradlew shadowJar

FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S scriletio && \
    adduser -S -H scriletio -G scriletio

WORKDIR /home/app

COPY --chown=scriletio:scriletio --from=build /build/libs/*-all.jar scriletio.jar

USER scriletio

ENTRYPOINT [ "java", "-jar", "scriletio.jar" ]