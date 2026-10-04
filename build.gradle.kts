import com.vanniktech.maven.publish.MavenPublishBaseExtension

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
    fun configurePublishing(artifact: String) {
        apply(plugin = "com.vanniktech.maven.publish")

        extensions.configure<MavenPublishBaseExtension> {
            coordinates(
                groupId = "me.znotchill.kiwi",
                artifactId = artifact,
                version = rootProject.version.toString()
            )

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

    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        configurePublishing(
            project.name
        )
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        configurePublishing(project.name)
    }
}