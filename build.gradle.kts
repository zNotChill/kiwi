plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}

allprojects {
    group = "me.znotchill.kiwi"
    version = "1.0.0"
}

subprojects {
    pluginManager.withPlugin("maven-publish") {
        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "znotchill"
                    url = uri("https://repo.znotchill.me/releases")

                    credentials {
                        username = project.findProperty("zRepoUsername") as String?
                            ?: System.getenv("MAVEN_USER")

                        password = project.findProperty("zRepoPassword") as String?
                            ?: System.getenv("MAVEN_PASS")
                    }
                }
            }

            publications {
                withType<MavenPublication> {
                    groupId = "me.znotchill.kiwi"

                    artifactId = when (project.name) {
                        "library" -> "core"
                        else -> project.name
                    }

                    version = project.version.toString()
                }
            }
        }
    }
}