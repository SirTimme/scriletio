package dev.sirtimme.gradle

dependencyLocking {
    lockAllConfigurations()
}

tasks.register("resolveAndLockAll") {
    notCompatibleWithConfigurationCache("Filters configurations at execution time")

    group = "verification"
    description = "Resolves all dependencies for all configurations."

    doFirst {
        require(gradle.startParameter.isWriteDependencyLocks) { "$path must be run from the command line with the `--write-locks` flag" }
    }

    doLast {
        configurations
            .filter { it.isCanBeResolved }
            .forEach { it.resolve() }
    }
}
