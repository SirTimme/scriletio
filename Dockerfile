FROM gradle:8.7.0-jdk21 AS build
ENV HOME=/home/gradle/src
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN gradle shadowJar

FROM openjdk:21
ENV HOME=/home/gradle/src
WORKDIR $HOME
COPY --from=build $HOME/build/libs/scriletio-*.jar scriletio.jar
ENTRYPOINT java -Dlogback.configurationFile=$HOME/cfg/logback.xml -jar scriletio.jar