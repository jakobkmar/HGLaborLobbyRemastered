import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

/*
 * PROJECT
 */

group = "net.axay"
version = "1.0.0"

plugins {

    kotlin("jvm") version "1.4.10"

    id("com.github.johnrengelman.shadow") version "6.0.0"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

    // SPIGOT
    compileOnly("org.spigotmc", "spigot", "1.16.3-R0.1-SNAPSHOT")

    // KSPIGOT
    implementation("net.axay", "KSpigot", "1.16.3_R11")

}

/*
 * BUILD
 */

tasks {
    shadowJar {
        minimize()
    }
}

val relocateShadowJar by tasks.creating(ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = "${project.group}.shadow"
}

tasks.shadowJar.get().dependsOn(relocateShadowJar)