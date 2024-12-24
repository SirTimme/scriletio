plugins {
    id("com.gradleup.shadow") version "8.3.3"
    java
}

group = "dev.sirtimme"
version = "0.0.11"

repositories {
    mavenCentral()
    maven {
        url = uri("https://patient-turkey-beloved.ngrok-free.app/releases")
    }
}

dependencies {
    implementation("dev.sirtimme:iuvo:0.0.7")
    implementation("ch.qos.logback:logback-classic:1.5.15")
    implementation("net.dv8tion:JDA:5.0.0") {
        exclude(group = "club.minnced", module = "opus-java")
    }
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.hibernate:hibernate-core:6.4.4.Final")
    implementation("org.hibernate:hibernate-hikaricp:6.4.4.Final")
    implementation("io.github.classgraph:classgraph:4.8.179")
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