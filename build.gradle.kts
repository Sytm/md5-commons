plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(libs.paper)
    compileOnly(libs.annotations)

    testImplementation(libs.junitJupiter)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get().toInt()))
    withSourcesJar()
    withJavadocJar()
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
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
