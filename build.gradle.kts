plugins {
    kotlin("jvm") version "1.9.23"
    application
    antlr
}

group = "io.github.minerofmillions"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    antlr("org.antlr:antlr4:4.13.1")
    compileOnly("org.antlr:antlr4-runtime:4.13.1")
}

tasks.compileKotlin {
    dependsOn("generateGrammarSource")
}

tasks.compileJava {
    dependsOn("generateGrammarSource")
}

tasks.compileTestKotlin {
    dependsOn("generateTestGrammarSource")
}

tasks.compileTestJava {
    dependsOn("generateTestGrammarSource")
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
