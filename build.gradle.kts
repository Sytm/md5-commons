plugins {
    `java-library`
    `maven-publish`
}

group = "de.md5lukas"
version = "2.1.0"
description = "md5-commons"

repositories {
    mavenCentral()

    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            name = "md5lukasReposilite"

            url = uri("https://repo.md5lukas.de/${if (version.toString().endsWith("-SNAPSHOT")) {
                "snapshots"
            } else {
                "releases"
            }}")
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

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}
