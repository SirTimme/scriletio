FROM gradle:8.6.0-jdk21-alpine as build
ENV HOME=/home/gradle/src
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN gradle shadowJar

FROM openjdk:21
ENV HOME=/home/gradle/src
WORKDIR $HOME
COPY --from=build $HOME/build/libs/scriletio-0.0.1-all.jar scriletio.jar
CMD java -jar scriletio.jar