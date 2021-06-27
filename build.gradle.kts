import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val jvmVersion = JavaVersion.VERSION_11
val jvmVersionString = jvmVersion.majorVersion.let { if (it.toInt() <= 10) "1.$it" else it }

group = "net.axay"
version = "1.0.0"

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("plugin.serialization") version "1.5.10"
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.cloudnetservice.eu/repository/releases/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.spigotmc", "spigot", "1.16.5-R0.1-SNAPSHOT")
    implementation("net.axay:kspigot:1.16.26")
    implementation("net.axay", "BlueUtils", "1.0.2")
    implementation("org.litote.kmongo", "kmongo-core", "4.2.7")
    implementation("org.litote.kmongo:kmongo-coroutine-core:4.2.7")
    implementation("de.hglabor:hglabor-utils:0.0.12")
    implementation("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.7")
    compileOnly("net.luckperms:api:5.3")

    compileOnly ("de.dytanic.cloudnet:cloudnet-bridge:3.3.0-RELEASE")
    compileOnly ("de.dytanic.cloudnet:cloudnet:3.3.0-RELEASE")
    compileOnly ("de.dytanic.cloudnet:cloudnet-wrapper-jvm:3.3.0-RELEASE")
}

java.sourceCompatibility = jvmVersion
java.targetCompatibility = jvmVersion

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}

tasks {
    shadowJar {
        //dependencies {
        //    exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib.*"))
        //}
        //minimize()
        simpleRelocate("net.axay.kspigot")
        //simpleRelocate("org.litote.kmongo.kmongo-core")
        //simpleRelocate("org.litote.kmongo.kmongo-serialization-mapping")
    }
}

fun ShadowJar.simpleRelocate(pattern: String) {
    relocate(pattern, "${project.group}.${project.name.toLowerCase()}.shadow.$pattern")
}
