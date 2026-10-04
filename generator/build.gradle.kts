import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

group = "me.znotchill.kiwi"
version = "unspecified"

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