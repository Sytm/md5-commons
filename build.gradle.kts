plugins {
    `java-library`
    `maven-publish`
}

group = "de.md5lukas"
version = "2.0.0-SNAPSHOT"
description = "md5-commons"

repositories {
    mavenLocal()
    mavenCentral()

    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    api("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}
