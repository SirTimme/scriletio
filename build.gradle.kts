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
	implementation(libs.logback)
	implementation(libs.jda) {
		exclude(group = "club.minnced", module = "opus-java")
	}
	implementation(libs.postgres)
	implementation(libs.hibernate.core)
	implementation(libs.hibernate.hikaricp)
}

tasks.jar {
	manifest {
		attributes["Main-Class"] = "dev.sirtimme.scriletio.Main"
	}
}