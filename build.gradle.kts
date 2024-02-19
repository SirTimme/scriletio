plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.sirtimme"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.20")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.sirtimme.Main"
    }
}