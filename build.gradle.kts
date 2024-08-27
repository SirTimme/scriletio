plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("dev.sirtimme.scriletio.open-telemetry")
}

group = "dev.sirtimme"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("dev.sirtimme:iuvo:0.0.5")
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("net.dv8tion:JDA:5.0.0") {
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.hibernate:hibernate-core:6.4.4.Final")
    implementation("org.hibernate:hibernate-hikaricp:6.4.4.Final")
}