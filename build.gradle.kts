import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(libs.plugins) {
        alias(kotlin)
        alias(dokka)
        alias(spotless)
    }
    `maven-publish`
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(libs.stdlib)
    api(libs.paper)
    compileOnly(libs.annotations)

    testImplementation(libs.kotlinJupiter)
    testImplementation(libs.junitJupiter)
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchain.get().toInt())
}

spotless {
    kotlin {
        ktfmt()
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs.addAll(
        "-Xjvm-default=all",
        "-Xlambdas=indy",
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "md5lukasReposilite"

            url = uri(
                "https://repo.md5lukas.de/${
                    if (version.toString().endsWith("-SNAPSHOT")) {
                        "snapshots"
                    } else {
                        "releases"
                    }
                }"
            )
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications.create<MavenPublication>("maven") {
        from(components["kotlin"])
    }
}
