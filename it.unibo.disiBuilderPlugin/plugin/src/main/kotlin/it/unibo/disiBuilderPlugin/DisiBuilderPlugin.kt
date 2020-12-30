/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package it.unibo.disiBuilderPlugin

import org.gradle.api.Project
import org.gradle.api.Plugin
import unibo.disi.builder.generator

/**
 * A simple 'hello world' plugin.
 */
class DisiBuilderPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // Register a task
        project.tasks.register("buildDisi") { task ->
            task.doLast {
                println("Hello from plugin DisiBuilderPlugin ")
                val s = generator.genFilePathName("pluto")
                println("Hello from disiBuilder plugin $s ")
            }
        }
    }
}
