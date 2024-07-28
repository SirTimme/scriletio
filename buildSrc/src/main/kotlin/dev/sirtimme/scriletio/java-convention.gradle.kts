package dev.sirtimme.scriletio

plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.named<Jar>("jar").configure {
    manifest {
        attributes["Main-Class"] = "dev.sirtimme.scriletio.Main"
    }
}