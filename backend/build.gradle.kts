plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.2.1"
    id("io.micronaut.aot") version "4.2.1"
    id("checkstyle")
    id("jacoco")
}

version = "0.1"
group = "broccoli"

val keycloakVersion by extra("23.0.3")
val hibernateVersion by extra("6.2.13.Final")
val logbackGelfVersion by extra("5.0.1")
val minioVersion by extra("8.5.7")
val testcontainersKeycloakVersion by extra("3.2.0")

configure<CheckstyleExtension> {
    toolVersion = "10.12.5"
    enableExternalDtdLoad = true
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:${hibernateVersion}")

    implementation("com.ongres.scram:client")
    implementation("de.siegmar:logback-gelf:${logbackGelfVersion}")
    implementation("io.micrometer:context-propagation")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.graphql:micronaut-graphql")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.security:micronaut-security-oauth2")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut:micronaut-management")
    implementation("io.minio:minio:${minioVersion}")
    implementation("org.keycloak:keycloak-admin-client:${keycloakVersion}")
    implementation("org.keycloak:keycloak-policy-enforcer:${keycloakVersion}")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
    compileOnly("io.micronaut:micronaut-http-client")
    compileOnly("org.projectlombok:lombok")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.postgresql:postgresql")

    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation("com.github.dasniko:testcontainers-keycloak:${testcontainersKeycloakVersion}")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.platform:junit-platform-suite-api")
    testImplementation("org.junit.platform:junit-platform-suite-engine")
    testImplementation("org.keycloak:keycloak-test-helper:${keycloakVersion}")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClass.set("broccoli.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("broccoli.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks.jacocoTestReport {

    reports {
        xml.required = true
    }

    // tests are required to run before generating the report
    dependsOn(tasks.test)

    classDirectories.setFrom(sourceSets.main.get().output.asFileTree.matching {
        exclude("broccoli/model/**/*_.*")
    })
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.test {
    useJUnitPlatform()

    // Set system properties for JUnit Platform
    finalizedBy(tasks.jacocoTestReport)
}
