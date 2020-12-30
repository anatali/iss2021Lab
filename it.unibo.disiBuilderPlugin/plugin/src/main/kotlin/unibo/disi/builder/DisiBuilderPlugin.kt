package unibo.disi.builder

import org.gradle.api.Plugin
import org.gradle.api.Project

class DisiBuilderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("buildDisi") {
            it.doLast{
                val s = generator.genFilePathName("pluto")
                 println("disiBuilder plugin | generates the path-name: $s ")

                //generator.genCodeFromModel("demo0")
            }
        }
        //project.task("buildActors")

    }
}