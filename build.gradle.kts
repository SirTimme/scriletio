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
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("net.dv8tion:JDA:5.0.0-beta.20") {
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.hibernate:hibernate-core:6.4.4.Final")
    implementation("org.hibernate:hibernate-hikaricp:6.4.4.Final")

    // OpenTelemetry
    implementation(platform("io.opentelemetry:opentelemetry-bom-alpha:1.40.0-alpha"))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.5.0-alpha"))

    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.sirtimme.scriletio.Main"
    }
}