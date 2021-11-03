plugins {
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
}

val bambooSpecArtifact = "com.atlassian.bamboo:bamboo-specs"
val bambooSpecVer = "6.9.2" //set your actual Bamboo instance version


dependencies {
    implementation("$bambooSpecArtifact-api:$bambooSpecVer")
    implementation("$bambooSpecArtifact:$bambooSpecVer")
    implementation(kotlin("stdlib-jdk8"))
}

group = "com.aut"
version = "1.0.0"
description = "bamboo-specs"

java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
