import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlinSerialization)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.obscure.computer/repository/maven-releases/")
    maven("https://repo.znotchill.me/repository/maven-releases/")
}

kotlin {
    jvm()

    macosArm64()
    linuxArm64()
    linuxX64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries {
            executable {
                entryPoint = "me.znotchill.kiwi.main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            // TODO: Make twine's data types support native
            // obviously twine won't support native itself, but it'd be good
            // for twine's data types to support native, so some classes
            // themselves can be a TwineNative wrapper instead of needing Vec2 and
            // LuaVec2Instance for example
            api("computer.obscure:twine:3.1.5")
            implementation(libs.kotlinx.serialization.json)
        }
        jvmMain.dependencies {
            implementation("net.kyori:adventure-text-minimessage:${project.property("adventure_version")}")
            implementation("net.kyori:adventure-api:${project.property("adventure_version")}")

            implementation("org.jetbrains.kotlin:kotlin-reflect")
        }
    }
}