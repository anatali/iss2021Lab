/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package it.unibo.disiBuilderPlugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'unibo.disi.builder' plugin.
 */
class ItUniboDisiBuilderPluginPluginTest {

    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        //project.plugins.apply("it.unibo.disiBuilderPlugin.greeting")
        project.plugins.apply("unibo.disi.builder")
        // Verify the result
        assertNotNull(project.tasks.findByName("buildDisiTest"))
        assertNotNull(project.tasks.findByName("projectInfo"))
        assertNotNull(project.tasks.findByName("buildActorDisi"))
        assertNotNull(project.tasks.findByName("buildQakSystem"))
    }
}
