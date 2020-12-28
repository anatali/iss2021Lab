package unibo.disi.builder

import org.gradle.api.Plugin
import org.gradle.api.Project

class DisiBuilderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("buildDisi") {
            doLast {

                val s = generator.genFilePathName("pluto")
                 println("Hello from disiBuilder plugin $s ")

                generator.genCodeFromModel("demo0")
            }
        }
    }
}