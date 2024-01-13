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
    }
}

project(":backend") {

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    sonar {
        properties {
            property("sonar.projectBaseDir", layout.projectDirectory.asFile.absolutePath)
            property("sonar.java.binaries", layout.buildDirectory.dir("classes").get().asFile.absolutePath)
            property("sonar.coverage.jacoco.xmlReportPaths", layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath)
            property("sonar.sources", layout.projectDirectory.dir("src/main").asFile.absolutePath)
            property("sonar.tests", layout.projectDirectory.dir("src/test").asFile.absolutePath)
        }
    }
}