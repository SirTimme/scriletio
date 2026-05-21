package dev.sirtimme.gradle

plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Main-Class"] = "dev.sirtimme.scriletio.Main"
    }
}
