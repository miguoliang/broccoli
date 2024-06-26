import com.github.gradle.node.pnpm.task.PnpmTask

plugins {
    id("com.github.node-gradle.node") version "7.0.1"
}

tasks.register<PnpmTask>("build") {
    group = "build"
    description = "Builds the frontend"

    args.set(arrayListOf("run", "build"))

    dependsOn("pnpmInstall")
}

tasks.register("clean") {
    group = "build"
    description = "Cleans the frontend"

    doLast {
        file("build").deleteRecursively()
        file("node_modules").deleteRecursively()
        file("index.css").delete()
    }
}