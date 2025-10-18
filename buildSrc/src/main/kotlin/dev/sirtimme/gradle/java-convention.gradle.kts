package dev.sirtimme.gradle

plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Main-Class"] = "dev.sirtimme.scriletio.Main"
    }
}