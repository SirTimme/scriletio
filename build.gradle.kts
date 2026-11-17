plugins {
    id("com.gradleup.shadow") version "9.4.1"
    id("dev.sirtimme.gradle.java-conventions")
    id("dev.sirtimme.gradle.dependency-conventions")
    id("dev.sirtimme.gradle.test-conventions")
}

group = "dev.sirtimme"
version = "0.0.13"

repositories {
    mavenCentral()
    maven {
        url = uri("https://forgejo.sirtimme.dev/api/packages/sirtimme/maven")
    }
}

dependencies {
    implementation("dev.sirtimme:iuvo:0.0.10")
    implementation("ch.qos.logback:logback-classic:1.5.34")
    implementation("net.dv8tion:JDA:6.4.2") {
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("org.hibernate.orm:hibernate-core:7.4.0.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:7.4.0.Final")
    implementation("io.github.classgraph:classgraph:4.8.184")

    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
