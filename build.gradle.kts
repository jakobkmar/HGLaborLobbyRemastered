@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_1_8
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

    kotlin("jvm") version "1.4.10"

    id("com.github.johnrengelman.shadow") version "6.0.0"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
    maven("https://jitpack.io")
}

dependencies {

    // SPIGOT
    compileOnly("org.spigotmc", "spigot", "1.16.3-R0.1-SNAPSHOT")

    // KSPIGOT
    implementation("net.axay", "KSpigot", "1.16.3_R15")

    // BLUEUTILS
    implementation("net.axay", "BlueUtils", "1.0.0")

    // KMONGO and MONGODB
    implementation("org.litote.kmongo", "kmongo-core", "4.1.3")
    implementation("org.litote.kmongo", "kmongo-serialization-mapping", "4.1.3")

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
        minimize {
            exclude(dependency("org.litote.kmongo:.*:.*"))
        }
    }
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString get() = majorVersion.let {
    val version = it.toInt()
    if (version <= 10) "1.$it" else it
}

fun KotlinCompile.configureJvmVersion() { kotlinOptions.jvmTarget = JVM_VERSION_STRING }