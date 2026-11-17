package dev.sirtimme.gradle

val testSourceSet = project.extensions.getByType<SourceSetContainer>().named("test")

tasks.register<Test>("unitTest") {
    group = "verification"
    description = "Runs the unit tests."

    classpath = files(testSourceSet.map { it.runtimeClasspath })
    testClassesDirs = files(testSourceSet.map { it.output.classesDirs })

    useJUnitPlatform {
        includeTags("unitTest")
    }
}

tasks.register<Test>("integrationTest") {
    group = "verification"
    description = "Runs the integration tests."

    classpath = files(testSourceSet.map { it.runtimeClasspath })
    testClassesDirs = files(testSourceSet.map { it.output.classesDirs })

    useJUnitPlatform {
        includeTags("integrationTest")
    }
}

tasks.named<Test>("test") {
    dependsOn("unitTest")
    dependsOn("integrationTest")
}
