plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.znotchill.me/releases")
    maven("https://redirector.kotlinlang.org/maven/bootstrap")
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content {
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
}

dependencies {
    compileOnly("org.slf4j:slf4j-api:2.0.17")
    compileOnly("net.minestom:minestom:${project.property("minestom_version")}")
    compileOnly("net.kyori:adventure-text-minimessage:4.26.1")
    compileOnly(project(":core"))
}

kotlin {
    jvmToolchain(25)
}