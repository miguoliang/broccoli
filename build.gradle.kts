plugins {
    id("org.sonarqube") version "4.4.1.3373"
}

group = "broccoli"
version = "0.1"

sonar {
    properties {
        property("sonar.projectKey", "miguoliang_broccoli")
        property("sonar.organization", "miguoliang")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.gradle.skipCompile", "true")
    }
}