plugins {
    id("com.gradleup.shadow") version "8.3.3"
    id("dev.sirtimme.gradle.java-convention")
}

group = "dev.sirtimme"
version = "0.0.12"

repositories {
    mavenCentral()
    maven {
        url = uri("https://patient-turkey-beloved.ngrok-free.app/releases")
    }
}

dependencies {
    implementation("dev.sirtimme:iuvo:0.0.8")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("net.dv8tion:JDA:5.6.1") {
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.hibernate:hibernate-core:7.0.5.Final")
    implementation("org.hibernate:hibernate-hikaricp:7.0.5.Final")
    implementation("io.github.classgraph:classgraph:4.8.180")
}