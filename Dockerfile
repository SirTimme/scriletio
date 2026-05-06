FROM eclipse-temurin:25 AS build

WORKDIR /home/gradle

COPY . .

RUN --mount=type=secret,id=FORGEJO_USERNAME,env=FORGEJO_USERNAME \
    --mount=type=secret,id=FORGEJO_ACCESS_TOKEN,env=FORGEJO_ACCESS_TOKEN \
    ./gradlew shadowJar

FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S scriletio && \
    adduser -S -H scriletio -G scriletio

WORKDIR /home/app

COPY --chown=scriletio:scriletio --from=build /home/gradle/build/libs/*-all.jar scriletio.jar

USER scriletio

ENTRYPOINT [ "java", "-jar", "scriletio.jar" ]