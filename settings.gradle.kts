rootProject.name="broccoli"

System.setProperty("sonar.gradle.skipCompile", "true")
System.setProperty("junit.jupiter.execution.parallel.enabled", "true")
System.setProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")

include("backend")
include("frontend")
