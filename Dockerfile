FROM eclipse-temurin:25 AS build

WORKDIR /home/gradle

COPY gradle gradle
COPY gradlew gradlew
RUN ./gradlew --version

COPY buildSrc buildSrc
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
RUN ./gradlew dependencies

COPY src src
RUN ./gradlew shadowJar

FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S scriletio && \
    adduser -S -H scriletio -G scriletio

WORKDIR /home/app

COPY --chown=scriletio:scriletio --from=build /home/gradle/build/libs/*-all.jar scriletio.jar

USER scriletio

ENTRYPOINT [ "java", "-jar", "scriletio.jar" ]