
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlinSerialization)
}

group = "me.znotchill.kiwi"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.obscure.computer/repository/maven-releases/")
    maven("https://repo.znotchill.me/releases/")
}

kotlin {
    jvm()

    macosArm64()
    linuxArm64()
    linuxX64()
    mingwX64()

    sourceSets {
        nativeMain.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.network)
            implementation(libs.ktor.network.tls)
        }
    }
}