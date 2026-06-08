plugins {
    id("com.gradleup.shadow") version "9.4.1"
    id("dev.sirtimme.gradle.java-conventions")
    id("dev.sirtimme.gradle.dependency-conventions")
}

group = "dev.sirtimme"
version = "0.0.12"

repositories {
    mavenCentral()
    maven {
        url = uri("https://forgejo.sirtimme.dev/api/packages/sirtimme/maven")
    }
}

dependencies {
    implementation("dev.sirtimme:iuvo:0.0.9")
    implementation("ch.qos.logback:logback-classic:1.5.34")
    implementation("net.dv8tion:JDA:6.4.2") {
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("org.hibernate:hibernate-core:7.3.3.Final")
    implementation("org.hibernate:hibernate-hikaricp:7.3.3.Final")
    implementation("io.github.classgraph:classgraph:4.8.184")
}
