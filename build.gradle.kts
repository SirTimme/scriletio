plugins {
    id("com.gradleup.shadow") version "8.3.3"
    id("dev.sirtimme.gradle.java-convention")
}

group = "dev.sirtimme"
version = "0.0.12"

repositories {
    mavenCentral()
    maven {
        url = uri("https://artifactory.sirtimme.dev/releases")
    }
}

dependencies {
    implementation("dev.sirtimme:iuvo:0.0.9")
    implementation("ch.qos.logback:logback-classic:1.5.19")
    implementation("net.dv8tion:JDA:5.6.1") {
    implementation("ch.qos.logback:logback-classic:1.5.32")
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("org.hibernate:hibernate-core:7.1.4.Final")
    implementation("org.hibernate:hibernate-hikaricp:7.1.4.Final")
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("org.hibernate:hibernate-core:7.3.3.Final")
    implementation("org.hibernate:hibernate-hikaricp:7.3.3.Final")
    implementation("io.github.classgraph:classgraph:4.8.184")
}
