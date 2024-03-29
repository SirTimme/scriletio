FROM gradle:8.6.0-jdk21-alpine as build
ENV HOME=/home/gradle/src
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN gradle shadowJar

FROM openjdk:21
ENV HOME=/home/gradle/src
WORKDIR $HOME
ARG TOKEN=""
ENV TOKEN=$TOKEN
ARG POSTGRES_USER=""
ENV POSTGRES_USER=$POSTGRES_USER
ARG POSTGRES_PASSWORD=""
ENV POSTGRES_PASSWORD=$POSTGRES_PASSWORD
ARG POSTGRES_URL=""
ENV POSTGRES_URL=$POSTGRES_URL
ARG OWNER_ID=""
ENV OWNER_ID=$OWNER_ID
COPY --from=build $HOME/build/libs/scriletio-0.0.1-all.jar scriletio.jar
CMD java -jar scriletio.jar