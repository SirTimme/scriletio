package dev.sirtimme.scriletio

plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Main-Class"] = "dev.sirtimme.scriletio.Main"
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}