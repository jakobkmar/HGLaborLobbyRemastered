import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val jvmVersion = JavaVersion.VERSION_11
val jvmVersionString = jvmVersion.majorVersion.let { if (it.toInt() <= 10) "1.$it" else it }

group = "net.axay"
version = "1.0.0"

plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("plugin.serialization") version "1.4.21"
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly("org.spigotmc", "spigot", "1.16.5-R0.1-SNAPSHOT")
    implementation("net.axay:kspigot:1.16.26")
    compileOnly("net.axay", "BlueUtils", "1.0.2")
    compileOnly("org.litote.kmongo", "kmongo-core", "4.2.3")
    compileOnly("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.3")
    compileOnly("net.luckperms:api:5.3")
}

java.sourceCompatibility = jvmVersion
java.targetCompatibility = jvmVersion

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib.*"))
        }
        minimize()
        simpleRelocate("net.axay.kspigot")
        simpleRelocate("kotlinx.serialization")
    }
}

fun ShadowJar.simpleRelocate(pattern: String) {
    relocate(pattern, "${project.group}.${project.name.toLowerCase()}.shadow.$pattern")
}
