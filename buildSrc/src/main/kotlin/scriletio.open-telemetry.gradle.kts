plugins {
    id("scriletio.java-convention")
}

dependencies {
    // OpenTelemetry
    implementation(platform("io.opentelemetry:opentelemetry-bom-alpha:1.40.0-alpha"))
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.5.0-alpha"))
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv")
    implementation("io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0")
}