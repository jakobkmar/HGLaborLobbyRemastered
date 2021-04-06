@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_11
val JVM_VERSION_STRING = JVM_VERSION.versionString

/*
 * PROJECT
 */

group = "net.axay"
version = "1.0.0"

/*
 * PLUGINS
 */

plugins {

    kotlin("jvm") version "1.4.21"

    id("com.github.johnrengelman.shadow") version "6.1.0"

    kotlin("plugin.serialization") version "1.4.21"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    jcenter()
    mavenLocal()
    mavenCentral()
    // AnvilGUI
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {

    // SPIGOT
    compileOnly("org.spigotmc", "spigot", "1.16.5-R0.1-SNAPSHOT")

    // KSPIGOT
    implementation("net.axay:kspigot:1.16.26")

    // BLUEUTILS
    compileOnly("net.axay", "BlueUtils", "1.0.2")

    // KMONGO and MONGODB
    compileOnly("org.litote.kmongo", "kmongo-core", "4.2.3")
    compileOnly("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.3")

}

/*
 * BUILD
 */

// JVM VERSION

java.sourceCompatibility = JVM_VERSION
java.targetCompatibility = JVM_VERSION

tasks.withType<KotlinCompile> {
    configureJvmVersion()
    configureJvmVersion()
}

// SHADOW

tasks {
    shadowJar {

        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib.*"))
        }

        minimize()

        simpleRelocate("net.axay.kspigot")

    }
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString
    get() = majorVersion.let {
        val version = it.toInt()
        if (version <= 10) "1.$it" else it
    }

fun KotlinCompile.configureJvmVersion() {
    kotlinOptions.jvmTarget = JVM_VERSION_STRING
}

fun ShadowJar.simpleRelocate(pattern: String) {
    relocate(pattern, "${project.group}.${project.name.toLowerCase()}.shadow.$pattern")
}