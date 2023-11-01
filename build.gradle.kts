plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.cbrentharris"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    // Usually we would reserve these for the testImplementation configuration, but we need them
    // in the main configuration to run the scaffolder.
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    implementation("org.assertj:assertj-core:3.24.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.register<JavaExec>("scaffold") {
    description = "Scaffolds a new day of Advent of Code."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "ScaffolderKt"
}