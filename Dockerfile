FROM eclipse-temurin:21-jdk-alpine AS build

COPY settings.gradle.kts build.gradle.kts gradlew /
COPY gradle gradle
COPY src src

RUN ./gradlew shadowJar

FROM eclipse-temurin:21-jre-alpine

ENV HOME=/home/app
ENV VERSION=0.0.10

WORKDIR $HOME

COPY --from=build /build/libs/scriletio-${VERSION}-all.jar $HOME/scriletio.jar

ENTRYPOINT [ "java", "--enable-preview", "-jar", "scriletio.jar" ]