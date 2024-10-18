plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

// Configure Kotlin JVM target
kotlin {
    jvmToolchain(22)  // Ensure this matches the version you're targeting
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))  // Ensure Java matches the Kotlin JVM version
    }
}