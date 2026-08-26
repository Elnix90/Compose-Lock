plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.vanniktech.maven.publish) apply false
}

allprojects {
    group = "io.github.elnix90.lock"
    version = "1.3.0"
}

subprojects {

    plugins.withId("com.vanniktech.maven.publish") {

        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {

            publishToMavenCentral()

            signAllPublications()

            pom {
                name.set(project.name)

                description.set(
                    "Compose Lock library"
                )

                inceptionYear.set("2026")

                url.set(
                    "https://github.com/Elnix90/compose-lock"
                )

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set(
                            "https://opensource.org/licenses/"
                        )
                    }
                }

                developers {
                    developer {
                        id.set("github")
                        name.set("Elnix")
                    }
                }

                scm {
                    url.set(
                        "https://github.com/Elnix90/compose-lock"
                    )

                    connection.set(
                        "scm:git:https://github.com/Elnix90/compose-lock.git"
                    )

                    developerConnection.set(
                        "scm:git:ssh://git@github.com/github/compose-lock.git"
                    )
                }
            }
        }
    }
}