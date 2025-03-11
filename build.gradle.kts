plugins {
    id("org.jetbrains.intellij") version "1.15.0"
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.squareup.okhttp3:okhttp:4.11.0") // OkHttp for HTTP requests
//    implementation("org.jetbrains:annotations:24.0.1")
}

intellij {
    version.set("2023.2.8")
    type.set("IC") // Target IDE Platform
    plugins.set(listOf("java")) // Include Java support
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    buildPlugin {
        archiveFileName.set("JavaAIErrorChecker.zip")
    }
}