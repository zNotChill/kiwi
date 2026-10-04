import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.kotlinpoet.jvm)
    implementation(kotlin("reflect"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}