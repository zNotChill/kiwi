import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.publish.PublishingExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}

allprojects {
    group = "me.znotchill.kiwi"
    version = "1.0.0"
}

subprojects {
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        apply(plugin = "com.vanniktech.maven.publish")

        extensions.configure<MavenPublishBaseExtension> {
            coordinates(
                groupId = "me.znotchill.kiwi",
                artifactId = project.name,
                version = rootProject.version.toString()
            )
        }
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        apply(plugin = "com.vanniktech.maven.publish")

        extensions.configure<MavenPublishBaseExtension> {
            coordinates(
                groupId = "me.znotchill.kiwi",
                artifactId = project.name,
                version = rootProject.version.toString()
            )
        }
    }

    pluginManager.withPlugin("maven-publish") {
        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "znotchill"
                    url = uri("https://repo.znotchill.me/releases")

                    credentials {
                        username = rootProject.findProperty("zRepoUsername") as String?
                            ?: System.getenv("MAVEN_USER")
                        password = rootProject.findProperty("zRepoPassword") as String?
                            ?: System.getenv("MAVEN_PASS")
                    }
                }
            }
        }
    }
}