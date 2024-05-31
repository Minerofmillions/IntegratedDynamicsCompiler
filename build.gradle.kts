plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "io.github.minerofmillions"
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
kotlin {
    jvmToolchain(19)
}

application {
    mainClass.set("io.github.minerofmillions.id_compiler.MainKt")
}